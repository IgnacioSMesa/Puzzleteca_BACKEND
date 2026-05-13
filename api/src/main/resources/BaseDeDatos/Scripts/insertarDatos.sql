SET search_path TO puzzles;

INSERT INTO usuario (nombre, apellido, email, contrasenna, tipousuario)
VALUES ('Natalia', 'García', 'natalia@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Usuario'),
       ('Carlos', 'López', 'carlos@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Usuario'),
       ('Sara', 'Martínez', 'sara@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Usuario'),
       ('Pablo', 'Sánchez', 'pablo@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Usuario');

INSERT INTO puzzle (titulo, autor, tiempo, piezas, dificultad, descripcion, color, valoracion_media, estado, id_usuario)
VALUES ('Atardecer en la playa', 'Ignacio Mesa', 600, 500, 'Facil',
        'Puzzle de un paisaje de playa al atardecer con tonos cálidos.', true, 5, 'Publico', 1),
       ('Gato curioso', 'Natalia García', 450, 300, 'Facil',
        'Imagen de un gato observando la cámara con fondo desenfocado.', true, 4, 'Publico', 2),
       ('Ciudad nocturna', 'Carlos López', 1200, 1000, 'Dificil', 'Vista aérea de una ciudad iluminada por la noche.',
        true, 5, 'Publico', 3),
       ('Bosque encantado', 'Sara Martínez', 900, 750, 'Media', 'Bosque mágico con niebla y luces entre los árboles.',
        true, 4, 'Publico', 4),
       ('Montañas nevadas', 'Pablo Sánchez', 700, 500, 'Media',
        'Paisaje de montañas cubiertas de nieve bajo cielo azul.', true, 5, 'Publico', 5),
       ('Patrones geométricos', 'Ignacio Mesa', 800, 600, 'Media', 'Puzzle abstracto con formas geométricas coloridas.',
        false, 3, 'Publico', 1),
       ('Perro jugando', 'Natalia García', 500, 400, 'Facil', 'Un perro jugando en un parque verde.', true, 4,
        'Publico', 2),
       ('Mar profundo', 'Carlos López', 1100, 900, 'Dificil', 'Fondo marino con corales y peces tropicales.', true, 5,
        'Publico', 3),
       ('Calle europea', 'Sara Martínez', 650, 500, 'Media',
        'Calle estrecha de una ciudad europea con edificios antiguos.', true, 4, 'Publico', 4),
       ('Espacio exterior', 'Pablo Sánchez', 1300, 1200, 'Dificil', 'Nebulosas y estrellas en el espacio profundo.',
        false, 5, 'Publico', 5),
       ('Flores de primavera', 'Ignacio Mesa', 400, 250, 'Facil', 'Campo lleno de flores de colores en primavera.',
        true, 4, 'Publico', 1),
       ('Desierto infinito', 'Natalia García', 1000, 800, 'Dificil', 'Dunas de arena bajo un cielo despejado.', false,
        4, 'Publico', 2);