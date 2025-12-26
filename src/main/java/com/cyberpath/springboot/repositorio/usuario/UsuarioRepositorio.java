package com.cyberpath.springboot.repositorio.usuario;

import com.cyberpath.springboot.modelo.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreCuenta(String nombreCuenta);
}
