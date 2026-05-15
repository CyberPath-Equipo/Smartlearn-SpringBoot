package com.cyberpath.smartlearn.repositorio.relaciones;

import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.modelo.relaciones.UsuarioMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioMateriaRepositorio extends JpaRepository<UsuarioMateria, Integer> {
    @Query("SELECT um.materia FROM UsuarioMateria um WHERE um.usuario.id = :userId")
    List<Materia> findMateriasByUsuarioId(@Param("userId") Integer userId);
}
