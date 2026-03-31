package com.cyberpath.springboot.controlador.usuario;

import com.cyberpath.springboot.controlador.usuario.contrasena.CambioPasswordDto;
import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.usuario.UsuarioDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Rol;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.usuario.UsuarioServicio;
import com.cyberpath.springboot.web.PasswordManager;
import com.cyberpath.springboot.web.jwt.JwtService;
import com.cyberpath.springboot.web.login.LoginRequest;
import com.cyberpath.springboot.web.login.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private final JwtService jwtService;
    private final PasswordManager passwordManager;

    @GetMapping("/usuario")
    public ResponseEntity<List<UsuarioDto>> lista() {
        List<Usuario> usuarios = usuarioServicio.getAll();
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UsuarioDto> dtos = usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> getById(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.getById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(usuario));
    }

    @PostMapping("/usuario")
    public ResponseEntity<UsuarioDto> save(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = mapDtoToEntity(usuarioDto);
        if (usuario.getContrasena() != null) {
            usuario.setContrasena(passwordManager.encode(usuario.getContrasena()));
        }
        Usuario guardado = usuarioServicio.save(usuario);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PostMapping("/usuario/login/docente")
    public ResponseEntity<?> loginDocente(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioServicio.getByCorreo(request.getCorreo());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        if (!passwordManager.validarContrasena(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contrasena incorrecta");
        }

        String token = jwtService.generarToken(usuario.getCorreo());

        LoginResponse response = new LoginResponse(
                token,
                usuario.getId(),
                usuario.getNombreCuenta(),
                usuario.getRol().getId()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/usuario/registro")
    public ResponseEntity<UsuarioDto> registrar(@RequestBody UsuarioDto newUser) {
        Usuario usuario = mapDtoToEntity(newUser);
        usuario.setContrasena(passwordManager.encode(newUser.getContrasena()));
        if (usuario.getActivo() == null) {
            usuario.setActivo(Boolean.TRUE);
        }
        if (usuario.getVerificado() == null) {
            usuario.setVerificado(Boolean.FALSE);
        }

        Usuario guardado = usuarioServicio.save(usuario);

        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        Usuario existente = usuarioServicio.getById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario datosActualizacion = mapDtoToEntity(usuarioDto);
        datosActualizacion.setId(id);
        if (usuarioDto.getContrasena() != null && !usuarioDto.getContrasena().isBlank()) {
            datosActualizacion.setContrasena(passwordManager.encode(usuarioDto.getContrasena()));
        } else {
            datosActualizacion.setContrasena(existente.getContrasena());
        }

        Usuario actualizado = usuarioServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @PutMapping("/usuario/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Integer id, @RequestBody CambioPasswordDto dto) {
        boolean actualizado = usuarioServicio.cambiarPassword(id, dto.getPasswordActual(), dto.getPasswordNueva());

        if (!actualizado) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{id}/materias")
    public ResponseEntity<List<MateriaDto>> getMateriasByUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.getById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        List<MateriaDto> materias = usuario.getUsuariosMaterias()
                .stream()
                .map(um -> {
                    Materia m = um.getMateria();
                    return MateriaDto.builder()
                            .id(m.getId())
                            .nombre(m.getNombre())
                            .slug(m.getSlug())
                            .descripcion(m.getDescripcion())
                            .createdAt(m.getCreatedAt())
                            .updatedAt(m.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(materias);
    }

    @GetMapping("/usuario/{idUsuario}/materia/{idMateria}/ejercicios-realizados")
    public ResponseEntity<Long> getEjerciciosRealizadosByUsuarioAndMateria(@PathVariable Integer idUsuario, @PathVariable Integer idMateria) {
        Usuario usuario = usuarioServicio.getById(idUsuario);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        Materia materia = usuarioServicio.getMateriaById(idMateria);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        Long cantidad = usuarioServicio.countEjerciciosRealizadosByUsuarioAndMateria(idUsuario, idMateria);
        return ResponseEntity.ok(cantidad);
    }

    @PostMapping("/usuario/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDto loginRequest) {
        if (loginRequest.getNombreCuenta() == null || loginRequest.getContrasena() == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioServicio.findByNombreCuenta(loginRequest.getNombreCuenta());
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        if (!passwordManager.validarContrasena(loginRequest.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contrasena incorrecta");
        }

        UsuarioDto response = convertToDto(usuario);
        response.setContrasena(null);
        return ResponseEntity.ok(response);
    }

    private UsuarioDto convertToDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombreCuenta(usuario.getNombreCuenta())
                .correo(usuario.getCorreo())
                .contrasena(usuario.getContrasena())
                .nombreCompleto(usuario.getNombreCompleto())
                .activo(usuario.getActivo())
                .verificado(usuario.getVerificado())
                .creadoEn(usuario.getCreadoEn())
                .actualizadoEn(usuario.getActualizadoEn())
                .idRol(usuario.getRol() != null ? usuario.getRol().getId() : null)
                .build();
    }

    private Usuario mapDtoToEntity(UsuarioDto dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nombreCuenta(dto.getNombreCuenta())
                .correo(dto.getCorreo())
                .contrasena(dto.getContrasena())
                .nombreCompleto(dto.getNombreCompleto())
                .activo(dto.getActivo())
                .verificado(dto.getVerificado())
                .creadoEn(dto.getCreadoEn())
                .actualizadoEn(dto.getActualizadoEn())
                .rol(dto.getIdRol() != null ? Rol.builder().id(dto.getIdRol()).build() : null)
                .build();
    }
}