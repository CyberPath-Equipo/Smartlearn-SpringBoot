package com.cyberpath.springboot.repositorio.contenido;

import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgresoSubtemaRepositorio extends JpaRepository<ProgresoSubtema, Integer> {
}
