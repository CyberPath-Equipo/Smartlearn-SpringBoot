package com.cyberpath.springboot.repositorio.recurso;

import com.cyberpath.springboot.modelo.recurso.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRecursoRepositorio extends JpaRepository<TipoRecurso, Integer> {
}
