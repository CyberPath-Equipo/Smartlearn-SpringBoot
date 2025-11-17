package com.cyberpath.springboot.repositorio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcionRepositorio extends JpaRepository<Opcion, Integer> {
}
