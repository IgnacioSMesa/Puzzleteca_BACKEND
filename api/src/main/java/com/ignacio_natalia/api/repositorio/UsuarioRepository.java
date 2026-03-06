package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>  {

    Optional<Usuario> findUsuarioByEmail(String email);

    @Modifying
    @Query("DELETE FROM Usuario u WHERE u.email = :email")
    int deleteByEmailReturnCount(@Param("email") String email);

}
