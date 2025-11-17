package com.cyberpath.springboot.repositorio.contenido;

import com.cyberpath.springboot.modelo.contenido.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia, Integer> {
}
