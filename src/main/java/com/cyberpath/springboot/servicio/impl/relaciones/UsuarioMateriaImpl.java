package com.cyberpath.springboot.servicio.impl.relaciones;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import org.springframework.stereotype.Service;
import com.cyberpath.springboot.repositorio.relaciones.UsuarioMateriaRepositorio;
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioMateriaServicio;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioMateriaImpl implements UsuarioMateriaServicio {
    private final UsuarioMateriaRepositorio usuarioMateriaRepositorio;

    @Override
    public List<UsuarioMateria> getAll() {
        return usuarioMateriaRepositorio.findAll();
    }

    @Override
    public UsuarioMateria getById(Integer id) {
        return usuarioMateriaRepositorio.findById(id).orElse(null);
    }

    @Override
    public UsuarioMateria save(UsuarioMateria usuarioMateria) {
        return usuarioMateriaRepositorio.save(usuarioMateria);
    }

    @Override
    public void delete(Integer id) {
        usuarioMateriaRepositorio.deleteById(id);
    }

    @Override
    public UsuarioMateria update(Integer id, UsuarioMateria usuarioMateria) {
        UsuarioMateria aux = usuarioMateriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Relacion UsuarioMateria no encontrada"));

        return usuarioMateriaRepositorio.save(aux);
    }
}