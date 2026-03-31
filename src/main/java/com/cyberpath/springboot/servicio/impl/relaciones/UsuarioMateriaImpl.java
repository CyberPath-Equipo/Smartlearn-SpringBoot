package com.cyberpath.springboot.servicio.impl.relaciones;

import com.cyberpath.springboot.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateriaId;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioMateriaServicio;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cyberpath.springboot.repositorio.relaciones.UsuarioMateriaRepositorio;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UsuarioMateriaImpl implements UsuarioMateriaServicio {
    private final UsuarioMateriaRepositorio usuarioMateriaRepositorio;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<UsuarioMateria> getAll() {
        return usuarioMateriaRepositorio.findAll();
    }

    @Override
    public UsuarioMateria getById(UsuarioMateriaId id) {
        return usuarioMateriaRepositorio.findById(id).orElse(null);
    }

    @Override
    public List<Materia> getMateriasByUser(Integer userId) {
        return usuarioMateriaRepositorio.findByUsuarioId(userId)
                .stream()
                .map(UsuarioMateria::getMateria)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioMateria save(UsuarioMateria usuarioMateria) {
        return usuarioMateriaRepositorio.save(usuarioMateria);
    }

    @Override
    public UsuarioMateria saveReferencia(UsuarioMateriaDto dto) {
        UsuarioMateria relacion = new UsuarioMateria();

        Usuario usuarioRef = entityManager.getReference(Usuario.class, dto.getIdUsuario());
        Materia materiaRef = entityManager.getReference(Materia.class, dto.getIdMateria());

        relacion.setUsuario(usuarioRef);
        relacion.setMateria(materiaRef);
        relacion.setSuscritoEn(dto.getSuscritoEn());

        return usuarioMateriaRepositorio.save(relacion);
    }

    @Override
    public void delete(UsuarioMateriaId id) {
        usuarioMateriaRepositorio.deleteById(id);
    }

    @Override
    public UsuarioMateria update(UsuarioMateriaId id, UsuarioMateria usuarioMateria) {
        UsuarioMateria aux = usuarioMateriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Relacion UsuarioMateria no encontrada"));

        if (usuarioMateria.getSuscritoEn() != null) {
            aux.setSuscritoEn(usuarioMateria.getSuscritoEn());
        }

        return usuarioMateriaRepositorio.save(aux);
    }
}