package com.cyberpath.springboot.repositorio.relaciones;

import com.cyberpath.springboot.modelo.relaciones.UsuarioEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioEjercicioRepositorio extends JpaRepository<UsuarioEjercicio, Integer> {
    // Ajustado: Cuenta todos los registros de UsuarioEjercicio para el usuario y materia (sin filtrar por hecho, ya que la presencia indica realizado)
    @Query("SELECT COUNT(ue) FROM UsuarioEjercicio ue WHERE ue.usuario.id = :idUsuario AND ue.ejercicio.subtema.tema.materia.id = :idMateria")
    Long countEjerciciosRealizadosByUsuarioAndMateria(@Param("idUsuario") Integer idUsuario, @Param("idMateria") Integer idMateria);
}