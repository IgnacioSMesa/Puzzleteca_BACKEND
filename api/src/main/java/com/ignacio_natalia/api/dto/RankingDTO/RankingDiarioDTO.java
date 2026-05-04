package com.ignacio_natalia.api.dto.RankingDTO;

/**
 * Proyección del ranking diario.
 * El constructor debe coincidir exactamente con el ORDER de columnas del @Query nativo.
 */
public class RankingDiarioDTO {

    private Integer idUsuario;
    private String  nombre;
    private String  apellido;
    private Double  mediaDiaria;
    private Long    totalValoraciones;

    // Constructor que usa Spring Data para mapear la query nativa
    public RankingDiarioDTO(Integer idUsuario,
                            String  nombre,
                            String  apellido,
                            Double  mediaDiaria,
                            Long    totalValoraciones) {
        this.idUsuario         = idUsuario;
        this.nombre            = nombre;
        this.apellido          = apellido;
        this.mediaDiaria       = mediaDiaria;
        this.totalValoraciones = totalValoraciones;
    }

    public Integer getIdUsuario()         { return idUsuario; }
    public String  getNombre()            { return nombre; }
    public String  getApellido()          { return apellido; }
    public Double  getMediaDiaria()       { return mediaDiaria; }
    public Long    getTotalValoraciones() { return totalValoraciones; }
}