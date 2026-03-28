package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {}