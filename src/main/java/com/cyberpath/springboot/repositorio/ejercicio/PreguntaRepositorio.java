package com.cyberpath.springboot.repositorio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreguntaRepositorio extends JpaRepository<Pregunta, Integer> {
}
