package com.cyberpath.smartlearn.servicio.implementacion.relaciones;

import com.cyberpath.smartlearn.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.modelo.relaciones.UsuarioMateria;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.repositorio.relaciones.UsuarioMateriaRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.relaciones.UsuarioMateriaServicio;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public UsuarioMateria findById(Integer id) {
        return usuarioMateriaRepositorio.findById(id).orElse(null);
    }

    @Override
    public List<Materia> getMateriasByUser(Integer userId) {
        return usuarioMateriaRepositorio.findMateriasByUsuarioId(userId);
    }

    @Override
    public UsuarioMateria save(UsuarioMateria usuarioMateria) {
        return usuarioMateriaRepositorio.save(usuarioMateria);
    }

    @Autowired
    private EntityManager entityManager;

    @Override
    public UsuarioMateria saveReferencia(UsuarioMateriaDto dto) {
        UsuarioMateria relacion = new UsuarioMateria();

        Usuario usuarioRef = entityManager.getReference(Usuario.class, dto.getIdUsuario());
        Materia materiaRef = entityManager.getReference(Materia.class, dto.getIdMateria());

        relacion.setUsuario(usuarioRef);
        relacion.setMateria(materiaRef);

        return usuarioMateriaRepositorio.save(relacion);
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