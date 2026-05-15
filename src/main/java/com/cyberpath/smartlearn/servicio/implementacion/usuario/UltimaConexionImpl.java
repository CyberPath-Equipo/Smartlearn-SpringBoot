package com.cyberpath.smartlearn.servicio.implementacion.usuario;

import com.cyberpath.smartlearn.modelo.usuario.UltimaConexion;
import com.cyberpath.smartlearn.repositorio.usuario.UltimaConexionRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UltimaConexionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UltimaConexionImpl implements UltimaConexionServicio {

    private final UltimaConexionRepositorio repositorio;

    @Override
    public List<UltimaConexion> getAll() {
        return repositorio.findAll();
    }

    @Override
    public UltimaConexion getById(Integer idUsuario) {
        return repositorio.findById(idUsuario).orElse(null);
    }

    @Override
    public UltimaConexion save(UltimaConexion conexion) {
        if (conexion.getUsuario() == null || repositorio.existsById(conexion.getUsuario().getId())) {
            throw new RuntimeException("Usuario inválido o ya tiene UltimaConexion");
        }
        return repositorio.save(conexion);
    }

    @Override
    public UltimaConexion update(Integer idUsuario, UltimaConexion datos) {
        UltimaConexion existente = repositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Conexión no encontrada"));

        existente.setUltimaConexion(datos.getUltimaConexion());
        existente.setDispositivo(datos.getDispositivo());
        existente.setSubtema(datos.getSubtema());

        return repositorio.save(existente);
    }

    @Override
    public void delete(Integer idUsuario) {
        repositorio.deleteById(idUsuario);
    }
}