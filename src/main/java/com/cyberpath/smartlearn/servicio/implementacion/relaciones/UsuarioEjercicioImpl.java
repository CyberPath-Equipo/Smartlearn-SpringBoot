package com.cyberpath.smartlearn.servicio.implementacion.relaciones;

import com.cyberpath.smartlearn.modelo.relaciones.UsuarioEjercicio;
import com.cyberpath.smartlearn.repositorio.relaciones.UsuarioEjercicioRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.relaciones.UsuarioEjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioEjercicioImpl implements UsuarioEjercicioServicio {
    private final UsuarioEjercicioRepositorio usuarioEjercicioRepositorio;

    @Override
    public List<UsuarioEjercicio> getAll() {
        return usuarioEjercicioRepositorio.findAll();
    }

    @Override
    public UsuarioEjercicio findById(Integer id) {
        return usuarioEjercicioRepositorio.findById(id).orElse(null);
    }

    @Override
    public UsuarioEjercicio save(UsuarioEjercicio usuarioEjercicio) {
        Integer idUsuario = usuarioEjercicio.getUsuario() != null ? usuarioEjercicio.getUsuario().getId() : null;
        Integer idEjercicio = usuarioEjercicio.getEjercicio() != null ? usuarioEjercicio.getEjercicio().getId() : null;

        if (idUsuario != null && idEjercicio != null) {
            UsuarioEjercicio existente = usuarioEjercicioRepositorio
                    .findByUsuarioIdAndEjercicioId(idUsuario, idEjercicio)
                    .orElse(null);

            if (existente != null) {
                existente.setHecho(usuarioEjercicio.getHecho());
                return usuarioEjercicioRepositorio.save(existente);
            }
        }

        try {
            return usuarioEjercicioRepositorio.save(usuarioEjercicio);
        } catch (DataIntegrityViolationException ex) {
            // Maneja condición de carrera: otra solicitud insertó la relación antes.
            if (idUsuario != null && idEjercicio != null) {
                UsuarioEjercicio existente = usuarioEjercicioRepositorio
                        .findByUsuarioIdAndEjercicioId(idUsuario, idEjercicio)
                        .orElseThrow(() -> ex);
                existente.setHecho(usuarioEjercicio.getHecho());
                return usuarioEjercicioRepositorio.save(existente);
            }
            throw ex;
        }
    }

    @Override
    public void delete(Integer id) {
        usuarioEjercicioRepositorio.deleteById(id);
    }

    @Override
    public UsuarioEjercicio update(Integer id, UsuarioEjercicio usuarioEjercicio) {
        UsuarioEjercicio aux = usuarioEjercicioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Relacion UsuarioEjercicio no encontrada"));

        aux.setUsuario(usuarioEjercicio.getUsuario());
        aux.setEjercicio(usuarioEjercicio.getEjercicio());
        aux.setHecho(usuarioEjercicio.getHecho());

        return usuarioEjercicioRepositorio.save(aux);
    }
}