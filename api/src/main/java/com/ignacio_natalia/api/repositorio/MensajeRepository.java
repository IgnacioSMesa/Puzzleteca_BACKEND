package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByConversacionIdOrderByCreadoEnAsc(Integer idConversacion);
}
