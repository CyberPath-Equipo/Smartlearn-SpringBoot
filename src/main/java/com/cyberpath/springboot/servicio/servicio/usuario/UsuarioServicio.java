package com.cyberpath.springboot.servicio.servicio.usuario;

import com.cyberpath.springboot.dto.usuario.UsuarioDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Usuario;

import java.util.List;

public interface UsuarioServicio {
    Usuario save(UsuarioDto usuarioDto);

    Usuario update(Integer id, UsuarioDto usuarioDto);

    void delete(Integer id);

    List<Usuario> getAll();

    Usuario findById(Integer id);

    Usuario findByNombreCuenta(String nombre);

    Usuario findByCorreo(String correo);

    Long countEjerciciosRealizadosByUsuarioAndMateria(Integer idUsuario, Integer idMateria);

    boolean cambiarPassword(Integer id, String passwordActual, String passwordNuevo);

    Materia getMateriaById(Integer idMateria);
}
