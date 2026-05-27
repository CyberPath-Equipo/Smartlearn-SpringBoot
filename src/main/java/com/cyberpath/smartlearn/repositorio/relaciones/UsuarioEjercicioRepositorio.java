package com.cyberpath.smartlearn.repositorio.relaciones;

import com.cyberpath.smartlearn.modelo.relaciones.UsuarioEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioEjercicioRepositorio extends JpaRepository<UsuarioEjercicio, Integer> {

    Optional<UsuarioEjercicio> findByUsuarioIdAndEjercicioId(Integer idUsuario, Integer idEjercicio);

    @Query("SELECT COUNT(ue) FROM UsuarioEjercicio ue WHERE ue.usuario.id = :idUsuario AND ue.ejercicio.subtema.tema.materia.id = :idMateria")
    Long countEjerciciosRealizadosByUsuarioAndMateria(@Param("idUsuario") Integer idUsuario, @Param("idMateria") Integer idMateria);
}