package com.cyberpath.springboot.repositorio.relaciones;

import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioMateriaRepositorio extends JpaRepository<UsuarioMateria, Integer> {
}
