package com.ignacio_natalia.api.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Servicio de procesamiento y almacenamiento de imágenes para posts.
 *
 * Estrategia de eficiencia para miles de usuarios:
 *  1. Redimensiona a max {@value #MAX_DIMENSION}px manteniendo aspecto.
 *  2. Comprime a JPEG con calidad {@value #JPEG_QUALITY} (~80-100 KB resultado).
 *  3. Guarda en disco bajo {@code app.upload.dir}/posts/.
 *  4. Devuelve la ruta relativa que se almacena en BD (TEXT corto, no base64).
 *
 * Ventajas vs base64 en BD:
 *  - La BD no crece desproporcionadamente (base64 infla ~33% y satura RAM del pool).
 *  - Las imágenes se sirven directamente por el servidor estático de Spring
 *    (o un CDN/nginx en producción) sin pasar por la JVM.
 *  - Si se migra a S3/CloudFront solo cambia este servicio.
 */
@Service
public class ImagenService {

    private static final int MAX_DIMENSION = 1080;
    private static final float JPEG_QUALITY = 0.82f;
    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    /** Tipos MIME aceptados */
    private static final Set<String> TIPOS_PERMITIDOS =
            Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    @Value("${app.upload.dir}")
    private String uploadDir;

    // -------------------------------------------------------------------------
    // API pública
    // -------------------------------------------------------------------------

    /**
     * Procesa y guarda la imagen de un post.
     *
     * @param archivo  MultipartFile recibido del frontend
     * @return ruta relativa almacenable en BD, ej. {@code posts/abc123.jpg}
     * @throws IOException              si falla la lectura/escritura
     * @throws IllegalArgumentException si el archivo no es una imagen válida
     */
    public String guardarImagenPost(MultipartFile archivo) throws IOException {

        validar(archivo);

        BufferedImage original = leerImagen(archivo);
        BufferedImage redimensionada = redimensionar(original);
        byte[] bytes = comprimirJpeg(redimensionada);

        String nombreArchivo = UUID.randomUUID() + ".jpg";
        String subRuta = "posts/" + nombreArchivo; // ruta relativa → se guarda en BD
        Path destino = Paths.get(uploadDir, subRuta);

        Files.createDirectories(destino.getParent());
        Files.write(destino, bytes, StandardOpenOption.CREATE_NEW);

        return subRuta; // ej: "posts/550e8400-e29b-41d4-a716-446655440000.jpg"
    }

    /**
     * Procesa y guarda la imagen de un puzzle.
     * Misma lógica que posts pero bajo la subcarpeta puzzles/.
     *
     * @param archivo  MultipartFile recibido del frontend
     * @return ruta relativa almacenable en BD, ej. {@code puzzles/abc123.jpg}
     */
    public String guardarImagenPuzzle(MultipartFile archivo) throws IOException {
        validar(archivo);
        BufferedImage original = leerImagen(archivo);
        BufferedImage redimensionada = redimensionar(original);
        byte[] bytes = comprimirJpeg(redimensionada);

        String nombreArchivo = UUID.randomUUID() + ".jpg";
        String subRuta = "puzzles/" + nombreArchivo;
        Path destino = Paths.get(uploadDir, subRuta);

        Files.createDirectories(destino.getParent());
        Files.write(destino, bytes, StandardOpenOption.CREATE_NEW);

        return subRuta;
    }

    /**
     * Elimina la imagen asociada a un post (llamar antes de borrar el Post de BD).
     *
     * @param imagenUrl ruta relativa almacenada en BD
     */
    public void eliminarImagen(String imagenUrl) {
        if (imagenUrl == null || imagenUrl.isBlank()) return;
        try {
            Path ruta = Paths.get(uploadDir, imagenUrl);
            Files.deleteIfExists(ruta);
        } catch (IOException ignored) {
            // No interrumpir el flujo principal por un archivo ya inexistente
        }
    }

    // -------------------------------------------------------------------------
    // Privados
    // -------------------------------------------------------------------------

    private void validar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty())
            throw new IllegalArgumentException("El archivo de imagen está vacío");

        if (archivo.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("La imagen supera el tamaño máximo permitido (10 MB)");

        String tipo = archivo.getContentType();
        System.out.println("TIPO DE LA IMAGEN: " + tipo);
        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo.toLowerCase()))
            throw new IllegalArgumentException("Tipo de imagen no permitido: " + tipo);
    }

    private BufferedImage leerImagen(MultipartFile archivo) throws IOException {
        try (InputStream is = archivo.getInputStream()) {
            BufferedImage img = ImageIO.read(is);
            if (img == null)
                throw new IllegalArgumentException("No se pudo decodificar la imagen");
            return img;
        }
    }

    // Para que todas las imagenes tengan el mismo tamaño
    private BufferedImage redimensionar(BufferedImage original) {
        int w = original.getWidth();
        int h = original.getHeight();

        if (w <= MAX_DIMENSION && h <= MAX_DIMENSION) return original;

        double escala = (double) MAX_DIMENSION / Math.max(w, h);
        int nuevoW = (int) Math.round(w * escala);
        int nuevoH = (int) Math.round(h * escala);

        // TYPE_INT_RGB → sin canal alfa, JPEG no soporta transparencia
        BufferedImage resultado = new BufferedImage(nuevoW, nuevoH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resultado.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, nuevoW, nuevoH);
        g.drawImage(original, 0, 0, nuevoW, nuevoH, null);
        g.dispose();
        return resultado;
    }

    private byte[] comprimirJpeg(BufferedImage imagen) throws IOException {
        // Si la imagen tiene canal alfa (PNG transparente) la aplanamos sobre blanco
        if (imagen.getType() != BufferedImage.TYPE_INT_RGB) {
            BufferedImage rgb = new BufferedImage(
                    imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgb.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, imagen.getWidth(), imagen.getHeight());
            g.drawImage(imagen, 0, 0, null);
            g.dispose();
            imagen = rgb;
        }

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) throw new IOException("No hay ImageWriter disponible para JPEG");
        ImageWriter writer = writers.next();

        ImageWriteParam params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(JPEG_QUALITY);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(imagen, null, null), params);
            writer.dispose();
            return baos.toByteArray();
        }
    }
}