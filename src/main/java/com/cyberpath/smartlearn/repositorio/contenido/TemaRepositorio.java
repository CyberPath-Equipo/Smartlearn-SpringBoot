package com.cyberpath.smartlearn.repositorio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemaRepositorio extends JpaRepository<Tema, Integer> {
}
