package com.cyberpath.springboot.repositorio.recurso;

import com.cyberpath.springboot.modelo.recurso.RecursoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoAdjuntoRepositorio extends JpaRepository<RecursoAdjunto, Integer> {
}
