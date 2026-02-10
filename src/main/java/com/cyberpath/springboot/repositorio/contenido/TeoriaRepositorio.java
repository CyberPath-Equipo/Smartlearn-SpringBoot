package com.cyberpath.springboot.repositorio.contenido;

import com.cyberpath.springboot.modelo.contenido.Teoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeoriaRepositorio extends JpaRepository<Teoria, Integer> {
}
