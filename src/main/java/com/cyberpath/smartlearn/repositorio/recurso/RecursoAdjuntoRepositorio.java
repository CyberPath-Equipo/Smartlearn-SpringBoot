package com.cyberpath.smartlearn.repositorio.recurso;

import com.cyberpath.smartlearn.modelo.recurso.RecursoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoAdjuntoRepositorio extends JpaRepository<RecursoAdjunto, Integer> {
}
