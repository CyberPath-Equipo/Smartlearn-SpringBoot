package com.cyberpath.springboot.servicio.impl.usuario;

import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import org.springframework.stereotype.Service;
import com.cyberpath.springboot.repositorio.usuario.UsuarioRepositorio;
import com.cyberpath.springboot.servicio.usuario.UsuarioServicio;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioImpl implements UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public List<Usuario> getAll() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Usuario getById(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public Usuario update(Integer id, Usuario usuario) {
        Usuario aux = usuarioRepositorio.findById(id).orElse(null);
        if (aux == null) {
            return null;
        }

        aux.setNombreCuenta(usuario.getNombreCuenta());
        aux.setCorreo(usuario.getCorreo());
        aux.setContrasena(usuario.getContrasena());
        aux.setRol(usuario.getRol());

        if (usuario.getConfiguracion() != null) {
            aux.setConfiguracion(usuario.getConfiguracion());
            usuario.getConfiguracion().setUsuario(aux);
        }

        if (usuario.getUltimaConexion() != null) {
            aux.setUltimaConexion(usuario.getUltimaConexion());
            usuario.getUltimaConexion().setUsuario(aux);
        }

        if (usuario.getIntentoEjercicio() != null) {
            for (IntentoEjercicio intento : usuario.getIntentoEjercicio()) {
                aux.addIntentoEjercicio(intento);
            }
        }

        if (usuario.getProgresoSubtema() != null) {
            for (ProgresoSubtema progreso : usuario.getProgresoSubtema()) {
                aux.addProgresoSubtema(progreso);
            }
        }

        if (usuario.getUsuariosMaterias() != null) {
            for (UsuarioMateria um : usuario.getUsuariosMaterias()) {
                aux.addUsuarioMateria(um);
            }
        }

        return usuarioRepositorio.save(aux);
    }
}