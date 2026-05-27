package com.cyberpath.smartlearn.servicio.implementacion.usuario;

import com.cyberpath.smartlearn.dto.usuario.UsuarioDto;
import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.modelo.usuario.Rol;
import com.cyberpath.smartlearn.modelo.usuario.UltimaConexion;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.repositorio.relaciones.UsuarioEjercicioRepositorio;
import com.cyberpath.smartlearn.repositorio.usuario.RolRepositorio;
import com.cyberpath.smartlearn.repositorio.usuario.UltimaConexionRepositorio;
import com.cyberpath.smartlearn.repositorio.usuario.UsuarioRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.contenido.MateriaServicio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UsuarioServicio;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class UsuarioImpl implements UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final UsuarioEjercicioRepositorio usuarioEjercicioRepositorio;
    private final MateriaServicio materiaServicio;
    private final UltimaConexionRepositorio ultimaConexionRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario save(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNombreCuenta(usuarioDto.getNombreCuenta());
        usuario.setNombreCompleto(usuarioDto.getNombreCompleto());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));
        Rol rol = rolRepositorio.findById(usuarioDto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);
        usuario.setVerificado(false);
        usuario.setCreadoEn(LocalDateTime.now());
        usuario.setActualizadoEn(LocalDateTime.now());

        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

        UltimaConexion ultimaConexion = UltimaConexion.builder()
                .ultimaConexion(LocalDateTime.now().toString())
                .dispositivo("default")
                .usuario(usuarioGuardado)
                .build();
        ultimaConexionRepositorio.save(ultimaConexion);

        return usuarioGuardado;
    }

    @Override
    public Usuario update(Integer id, UsuarioDto usuarioDto) {
        Usuario usuarioEditar = findById(id);
        if (usuarioEditar == null) return null;

        if (usuarioDto.getNombreCuenta() != null) usuarioEditar.setNombreCuenta(usuarioDto.getNombreCuenta());
        if (usuarioDto.getNombreCompleto() != null) usuarioEditar.setNombreCompleto(usuarioDto.getNombreCompleto());
        if (usuarioDto.getCorreo() != null) usuarioEditar.setCorreo(usuarioDto.getCorreo());

        if (usuarioDto.getContrasena() != null && !usuarioDto.getContrasena().isEmpty()) {
            usuarioEditar.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));
        }

        return usuarioRepositorio.save(usuarioEditar);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public List<Usuario> getAll() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Usuario findById(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    @Override
    public Usuario findByNombreCuenta(String nombreCuenta) {
        return usuarioRepositorio.findByNombreCuenta(nombreCuenta).orElse(null);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo).orElse(null);
    }

    @Override
    public Long countEjerciciosRealizadosByUsuarioAndMateria(Integer idUsuario, Integer idMateria) {
        return usuarioEjercicioRepositorio.countEjerciciosRealizadosByUsuarioAndMateria(idUsuario, idMateria);
    }

    @Override
    public Materia getMateriaById(Integer idMateria) {
        return materiaServicio.findById(idMateria);
    }

    @Override
    public boolean cambiarPassword(Integer id, String passwordActual, String passwordNueva) {
        Usuario usuario = usuarioRepositorio.findById(id).orElse(null);
        if (usuario == null) return false;

        if (!passwordEncoder.matches(passwordActual, usuario.getContrasena())) {
            return false;
        }

        usuario.setContrasena(passwordEncoder.encode(passwordNueva));
        usuarioRepositorio.save(usuario);
        return true;
    }

}