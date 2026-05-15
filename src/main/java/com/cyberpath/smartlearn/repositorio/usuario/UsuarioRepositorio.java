package com.cyberpath.smartlearn.repositorio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    @EntityGraph(attributePaths = {"rol"})
    Optional<Usuario> findByNombreCuenta(String nombreCuenta);
    @EntityGraph(attributePaths = {"rol"})
    Optional<Usuario> findByCorreo(String correo);
}
