package com.cyberpath.springboot.servicio.servicio.usuario;

import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Usuario;

import java.util.List;

public interface UsuarioServicio {
    List<Usuario> getAll();

    Usuario getById(Integer id);

    Usuario getByCorreo(String correo);

    Usuario save(Usuario usuario);

    void delete(Integer id);

    Usuario update(Integer id, Usuario usuario);

    Usuario findByNombreCuenta(String nombre);

    Long countEjerciciosRealizadosByUsuarioAndMateria(Integer idUsuario, Integer idMateria);

    boolean cambiarPassword(Integer id, String passwordActual, String passwordNuevo);

    Materia getMateriaById(Integer idMateria);
}
