package com.cyberpath.springboot.repositorio.usuario;

import com.cyberpath.springboot.modelo.usuario.UltimaConexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UltimaConexionRepositorio extends JpaRepository<UltimaConexion, Integer> {
}
