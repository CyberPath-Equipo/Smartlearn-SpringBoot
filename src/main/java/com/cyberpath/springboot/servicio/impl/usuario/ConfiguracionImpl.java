package com.cyberpath.springboot.servicio.impl.usuario;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.usuario.Configuracion;
import com.cyberpath.springboot.repositorio.usuario.ConfiguracionRepositorio;
import com.cyberpath.springboot.servicio.servicio.usuario.ConfiguracionServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConfiguracionImpl implements ConfiguracionServicio {
    private final ConfiguracionRepositorio configuracionRepositorio;

    @Override
    public List<Configuracion> getAll() {
        return configuracionRepositorio.findAll();
    }

    @Override
    public Configuracion getById(Integer id) {
        return configuracionRepositorio.findById(id).orElse(null);
    }

    @Override
    public Configuracion save(Configuracion configuracion) {
        return configuracionRepositorio.save(configuracion);
    }

    @Override
    public void delete(Integer id) {
        configuracionRepositorio.deleteById(id);
    }

    @Override
    public Configuracion update(Integer id, Configuracion configuracion) {
        Configuracion aux = configuracionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuracion no encontrada"));
        aux.setCuentaCreada(configuracion.isCuentaCreada());
        aux.setModoAudio(configuracion.isModoAudio());
        aux.setModoOffline(configuracion.isModoOffline());
        aux.setNotificacionesActivadas(configuracion.isNotificacionesActivadas());
        aux.setTamanoFuente(configuracion.getTamanoFuente());

        return configuracionRepositorio.save(aux);
    }
}