package com.cyberpath.springboot.repositorio.usuario;

import com.cyberpath.springboot.modelo.usuario.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionRepositorio extends JpaRepository<Configuracion, Integer> {
}
