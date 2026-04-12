package com.cyberpath.springboot.repositorio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioRepositorio extends JpaRepository<Ejercicio, Integer> {
}
