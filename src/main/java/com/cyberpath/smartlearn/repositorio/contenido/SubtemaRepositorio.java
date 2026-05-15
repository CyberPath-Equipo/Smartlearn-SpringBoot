package com.cyberpath.smartlearn.repositorio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.Subtema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtemaRepositorio extends JpaRepository<Subtema, Integer> {
}
