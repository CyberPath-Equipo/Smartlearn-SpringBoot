package com.cyberpath.springboot.repositorio.relaciones;

import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateriaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioMateriaRepositorio extends JpaRepository<UsuarioMateria, UsuarioMateriaId> {
    List<UsuarioMateria> findByUsuarioId(Integer idUsuario);
    List<UsuarioMateria> findByMateriaId(Integer idMateria);
}
