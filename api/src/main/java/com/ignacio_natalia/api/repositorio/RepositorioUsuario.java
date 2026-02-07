package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Integer>  {
    Optional<Usuario> findUsuarioByEmail(String email);

    boolean existsByEmail (String email);
}
