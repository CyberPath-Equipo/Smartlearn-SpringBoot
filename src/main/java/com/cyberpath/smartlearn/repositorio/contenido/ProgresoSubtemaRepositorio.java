package com.cyberpath.smartlearn.repositorio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.ProgresoSubtema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgresoSubtemaRepositorio extends JpaRepository<ProgresoSubtema, Integer> {
}
