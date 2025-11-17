package com.cyberpath.springboot.repositorio.usuario;

import com.cyberpath.springboot.modelo.usuario.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer> {
}
