package com.cyberpath.smartlearn.controlador.usuario;

import com.cyberpath.smartlearn.controlador.usuario.contrasena.CambioPasswordDto;
import com.cyberpath.smartlearn.dto.contenido.MateriaDto;
import com.cyberpath.smartlearn.dto.usuario.RegistroVerificacionRequest;
import com.cyberpath.smartlearn.dto.usuario.TwoFactorResendRequest;
import com.cyberpath.smartlearn.dto.usuario.UsuarioDto;
import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.modelo.usuario.Rol;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.servicio.servicio.relaciones.UsuarioMateriaServicio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UsuarioServicio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.TwoFactorServicio;
import com.cyberpath.smartlearn.configuracion.seguridad.jwt.JwtService;
import com.cyberpath.smartlearn.configuracion.seguridad.login.LoginRequest;
import com.cyberpath.smartlearn.configuracion.seguridad.login.LoginResponse;
import com.cyberpath.smartlearn.configuracion.seguridad.login.RefreshTokenRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
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
    private final TwoFactorServicio twoFactorServicio;

    @PostMapping("/usuario/registro")
    public ResponseEntity<?> save(@Valid @RequestBody UsuarioDto usuarioDto) {
        try {
            Usuario usuarioGuardar = usuarioServicio.save(usuarioDto);
            String transactionId = twoFactorServicio.createRegistrationTransaction(usuarioGuardar.getId());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                    "message", "Código de verificación enviado al correo registrado",
                    "requiresVerification", true,
                    "transactionId", transactionId,
                    "correo", usuarioGuardar.getCorreo()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/usuario/registro/verificar")
    public ResponseEntity<?> verifyRegistration(@RequestBody RegistroVerificacionRequest request) {
        if (request.getTransactionId() == null || request.getTransactionId().isBlank()
                || request.getCode() == null || request.getCode().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "transactionId y code son obligatorios"));
        }

        try {
            twoFactorServicio.verifyRegistrationCode(request.getTransactionId(), request.getCode());
            return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/usuario/registro/reenviar")
    public ResponseEntity<?> resendRegistrationCode(@RequestBody TwoFactorResendRequest request) {
        if (request.getTransactionId() == null || request.getTransactionId().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "transactionId es obligatorio"));
        }

        try {
            twoFactorServicio.resendRegistrationCode(request.getTransactionId());
            return ResponseEntity.ok(Map.of("message", "Código de verificación reenviado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
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

        if (!Boolean.TRUE.equals(usuario.getVerificado()) || !Boolean.TRUE.equals(usuario.getActivo())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Cuenta pendiente de verificación. Revisa tu correo e ingresa el código."));
        }

        if (usuario.getTwoFactorEnabled()) {
            String transactionId = twoFactorServicio.createTransaction(usuario.getId(), "EMAIL");
            
            return ResponseEntity.ok(LoginResponse.builder()
                    .requires2fa(true)
                    .twoFactorTransactionId(transactionId)
                    .twoFactorChannel("EMAIL")
                    .idUsuario(usuario.getId())
                    .nombreCuenta(usuario.getNombreCuenta())
                    .idRol(usuario.getRol().getId())
                    .build());
        }

        return ResponseEntity.ok(buildLoginResponse(usuario));
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

        if (!Boolean.TRUE.equals(usuario.getVerificado()) || !Boolean.TRUE.equals(usuario.getActivo())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Cuenta pendiente de verificación. Revisa tu correo e ingresa el código."));
        }

        // Si el usuario tiene 2FA habilitado
        if (usuario.getTwoFactorEnabled()) {
            String transactionId = twoFactorServicio.createTransaction(usuario.getId(), "EMAIL");
            
            return ResponseEntity.ok(LoginResponse.builder()
                    .requires2fa(true)
                    .twoFactorTransactionId(transactionId)
                    .twoFactorChannel("EMAIL")
                    .idUsuario(usuario.getId())
                    .nombreCuenta(usuario.getNombreCuenta())
                    .idRol(usuario.getRol().getId())
                    .build());
        }

        // Login normal sin 2FA
        return ResponseEntity.ok(buildLoginResponse(usuario));
    }

    @PostMapping("/usuario/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "refreshToken es obligatorio"));
        }

        if (!jwtService.isRefreshTokenValid(request.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token inválido o expirado"));
        }

        String correo = jwtService.obtenerSubject(request.getRefreshToken());
        Usuario usuario = usuarioServicio.findByCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        return ResponseEntity.ok(buildLoginResponse(usuario, request.getRefreshToken()));
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

    private LoginResponse buildLoginResponse(Usuario usuario) {
        return buildLoginResponse(usuario, jwtService.generarRefreshToken(usuario.getCorreo()));
    }

    private LoginResponse buildLoginResponse(Usuario usuario, String refreshToken) {
        return LoginResponse.builder()
                .token(jwtService.generarToken(usuario.getCorreo()))
                .refreshToken(refreshToken)
                .idUsuario(usuario.getId())
                .nombreCuenta(usuario.getNombreCuenta())
                .idRol(usuario.getRol().getId())
                .requires2fa(false)
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