package com.cyberpath.springboot.repositorio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntentoEjercicioRepositorio extends JpaRepository<IntentoEjercicio, Integer> {
}
