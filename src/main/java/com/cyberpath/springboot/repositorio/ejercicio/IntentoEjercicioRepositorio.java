package com.cyberpath.springboot.repositorio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IntentoEjercicioRepositorio extends JpaRepository<IntentoEjercicio, Integer> {
    @Query("SELECT COUNT(DISTINCT i.ejercicio.id) FROM IntentoEjercicio i WHERE i.usuario.id = :idUsuario AND i.ejercicio.subtema.tema.materia.id = :idMateria")
    Long countEjerciciosRealizadosByUsuarioAndMateria(@Param("idUsuario") Integer idUsuario, @Param("idMateria") Integer idMateria);
}
