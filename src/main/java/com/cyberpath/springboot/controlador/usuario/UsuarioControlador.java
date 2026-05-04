package com.cyberpath.springboot.controlador.usuario;

import com.cyberpath.springboot.controlador.usuario.contrasena.CambioPasswordDto;
import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.usuario.UsuarioDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Rol;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioMateriaServicio;
import com.cyberpath.springboot.servicio.servicio.usuario.UsuarioServicio;
import com.cyberpath.springboot.configuracion.seguridad.jwt.JwtService;
import com.cyberpath.springboot.configuracion.seguridad.login.LoginRequest;
import com.cyberpath.springboot.configuracion.seguridad.login.LoginResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Slf4j
public class UsuarioControlador {
    private final UsuarioServicio usuarioServicio;
    private final UsuarioMateriaServicio usuarioMateriaServicio;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/usuario/registro")
    public ResponseEntity<UsuarioDto> save(@Valid @RequestBody UsuarioDto usuarioDto) {
        Usuario usuarioGuardar = usuarioServicio.save(usuarioDto);
        return ResponseEntity.ok(convertToDto(usuarioGuardar));
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        Usuario usuarioActualizar = usuarioServicio.update(id, usuarioDto);
        if(usuarioActualizar == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(usuarioActualizar));
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<UsuarioDto>> getAll() {
        List<Usuario> usuarios = usuarioServicio.getAll();
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UsuarioDto> usuariosDto = usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDto);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> findById(@PathVariable Integer id, Principal principal) {
        Usuario usuarioSolicitante = usuarioServicio.findByCorreo(principal.getName());
        if (usuarioSolicitante == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!usuarioSolicitante.getRol().getTipo().equalsIgnoreCase("ADMIN")
                && !usuarioSolicitante.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario usuario = usuarioServicio.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(usuario));
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

        if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(401).build();
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


    @PostMapping("/usuario/login/docente")
    public ResponseEntity<?> loginDocente(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioServicio.findByCorreo(request.getCorreo());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
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

    @PutMapping("/usuario/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Integer id, @RequestBody CambioPasswordDto dto) {
        boolean actualizado = usuarioServicio.cambiarPassword(id, dto.getPasswordActual(), dto.getPasswordNueva());
        if (!actualizado) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("usuario/{id}/materias")
    public ResponseEntity<List<MateriaDto>> getMateriasByUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        List<MateriaDto> materias = usuarioMateriaServicio.getMateriasByUser(id)
                .stream()
                .map(m -> MateriaDto.builder()
                        .id(m.getId())
                        .nombre(m.getNombre())
                        .slug(m.getSlug())
                        .descripcion(m.getDescripcion())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(materias);
    }

    @GetMapping("/usuario/{idUsuario}/materia/{idMateria}/ejercicios-realizados")
    public ResponseEntity<Long> getEjerciciosRealizadosByUsuarioAndMateria(@PathVariable Integer idUsuario, @PathVariable Integer idMateria) {
        Usuario usuario = usuarioServicio.findById(idUsuario);
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

    private UsuarioDto convertToDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombreCuenta(usuario.getNombreCuenta())
                .correo(usuario.getCorreo())
                .nombreCompleto(usuario.getNombreCompleto())
                .activo(usuario.getActivo())
                .verificado(usuario.getVerificado())
                .idRol(usuario.getRol() != null ? usuario.getRol().getId() : null)
                .idConfiguracion(usuario.getConfiguracion() != null ? usuario.getConfiguracion().getId() : null)
                .idUltimaConexion(usuario.getUltimaConexion() != null ? usuario.getUltimaConexion().getId() : null)
                .build();
    }

    private Usuario mapDtoToEntity(UsuarioDto dto) {
        return Usuario.builder()
                .nombreCuenta(dto.getNombreCuenta())
                .correo(dto.getCorreo())
                .contrasena(dto.getContrasena())
                .nombreCompleto(dto.getNombreCompleto())
                .activo(dto.getActivo())
                .verificado(dto.getVerificado())
                .rol(dto.getIdRol() != null ? Rol.builder().id(dto.getIdRol()).build() : null)
                .build();
    }
}