package com.cyberpath.springboot.servicio.impl.usuario;

import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import com.cyberpath.springboot.modelo.usuario.UltimaConexion;
import com.cyberpath.springboot.repositorio.relaciones.UsuarioEjercicioRepositorio;
import com.cyberpath.springboot.repositorio.usuario.UltimaConexionRepositorio;
import com.cyberpath.springboot.servicio.servicio.contenido.MateriaServicio;
import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import org.springframework.stereotype.Service;
import com.cyberpath.springboot.repositorio.usuario.UsuarioRepositorio;
import com.cyberpath.springboot.servicio.servicio.usuario.UsuarioServicio;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioImpl implements UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioEjercicioRepositorio usuarioEjercicioRepositorio;
    private final MateriaServicio materiaServicio;
    private final UltimaConexionRepositorio ultimaConexionRepositorio;

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
        // Primero guarda el usuario para obtener su ID
        Usuario guardado = usuarioRepositorio.save(usuario);
        // Crea UltimaConexion SIN setear ID manualmente (deja que @MapsId lo haga)
        UltimaConexion ultimaConexion = UltimaConexion.builder()
                .ultimaConexion(LocalDateTime.now().toString())  // Fecha actual como String
                .dispositivo("default")  // Valor por defecto
                .usuario(guardado)  // Asigna el usuario (esto asigna el ID automáticamente)
                .build();
        UltimaConexion guardadaConexion = ultimaConexionRepositorio.save(ultimaConexion);
        // Si la relación es bidireccional, setea la referencia en Usuario
        guardado.setUltimaConexion(guardadaConexion);
        return guardado;
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

    @Override
    public Usuario findByNombreCuenta(String nombreCuenta) {
        return usuarioRepositorio.findByNombreCuenta(nombreCuenta).orElse(null);
    }

    @Override
    public Long countEjerciciosRealizadosByUsuarioAndMateria(Integer idUsuario, Integer idMateria) {
        return usuarioEjercicioRepositorio.countEjerciciosRealizadosByUsuarioAndMateria(idUsuario, idMateria);
    }
    @Override
    public Materia getMateriaById(Integer idMateria) {
        return materiaServicio.getById(idMateria);
    }
}