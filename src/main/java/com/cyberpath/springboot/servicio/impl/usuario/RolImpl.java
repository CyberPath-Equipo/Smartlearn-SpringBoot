package com.cyberpath.springboot.servicio.impl.usuario;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.usuario.Rol;
import com.cyberpath.springboot.repositorio.usuario.RolRepositorio;
import com.cyberpath.springboot.servicio.servicio.usuario.RolServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RolImpl implements RolServicio {
    private final RolRepositorio rolRepositorio;

    @Override
    public List<Rol> getAll() {
        return rolRepositorio.findAll();
    }

    @Override
    public Rol getById(Integer id) {
        return rolRepositorio.findById(id).orElse(null);
    }

    @Override
    public Rol save(Rol rol) {
        return rolRepositorio.save(rol);
    }

    @Override
    public void delete(Integer id) {
        rolRepositorio.deleteById(id);
    }

    @Override
    public Rol update(Integer id, Rol rol) {
        Rol aux = rolRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        aux.setTipo(rol.getTipo());

        return rolRepositorio.save(aux);
    }
}