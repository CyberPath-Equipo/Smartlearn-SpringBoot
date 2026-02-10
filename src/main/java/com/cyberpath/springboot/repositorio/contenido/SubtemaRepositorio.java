package com.cyberpath.springboot.repositorio.contenido;

import com.cyberpath.springboot.modelo.contenido.Subtema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtemaRepositorio extends JpaRepository<Subtema, Integer> {
}
