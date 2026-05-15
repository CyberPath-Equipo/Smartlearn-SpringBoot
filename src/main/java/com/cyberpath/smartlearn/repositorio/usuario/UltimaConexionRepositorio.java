package com.cyberpath.smartlearn.repositorio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.UltimaConexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UltimaConexionRepositorio extends JpaRepository<UltimaConexion, Integer> {
}
