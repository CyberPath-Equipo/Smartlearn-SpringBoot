package com.cyberpath.smartlearn.repositorio.ejercicio;

import com.cyberpath.smartlearn.modelo.ejercicio.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioRepositorio extends JpaRepository<Ejercicio, Integer> {
}
