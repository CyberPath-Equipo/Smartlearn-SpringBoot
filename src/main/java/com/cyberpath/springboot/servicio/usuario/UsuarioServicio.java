package com.cyberpath.springboot.servicio.usuario;

import com.cyberpath.springboot.modelo.usuario.Usuario;

import java.util.List;

public interface UsuarioServicio {
    List<Usuario> getAll( );
    Usuario getById(Integer id);
    Usuario getByCorreo(String correo);
    Usuario save(Usuario usuario);
    void delete(Integer id);
    Usuario update(Integer id, Usuario usuario);
    boolean cambiarPassword(Integer id, String passwordActual, String passwordNuevo);
}
