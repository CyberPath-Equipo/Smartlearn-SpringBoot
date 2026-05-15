package com.cyberpath.smartlearn.repositorio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.Teoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeoriaRepositorio extends JpaRepository<Teoria, Integer> {
}
