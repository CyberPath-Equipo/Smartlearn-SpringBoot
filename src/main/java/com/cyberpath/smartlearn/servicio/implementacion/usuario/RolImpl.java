package com.cyberpath.smartlearn.servicio.implementacion.usuario;

import com.cyberpath.smartlearn.modelo.usuario.Rol;
import com.cyberpath.smartlearn.repositorio.usuario.RolRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.RolServicio;
import lombok.AllArgsConstructor;
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
    public Rol findById(Integer id) {
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