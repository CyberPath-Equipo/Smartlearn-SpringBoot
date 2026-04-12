package com.cyberpath.springboot.repositorio.contenido;

import com.cyberpath.springboot.modelo.contenido.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia, Integer> {
    @Query("SELECT COUNT(e) FROM Ejercicio e WHERE e.subtema.tema.materia.id = :idMateria")
    Long countEjerciciosByMateriaId(@Param("idMateria") Integer idMateria);
}
