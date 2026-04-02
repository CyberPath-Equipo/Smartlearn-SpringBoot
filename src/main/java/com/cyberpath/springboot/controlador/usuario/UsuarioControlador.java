package com.cyberpath.springboot.controlador.usuario;

import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.ejercicio.EjercicioDto;
import com.cyberpath.springboot.dto.usuario.UsuarioDto;
import com.cyberpath.springboot.controlador.usuario.contrasena.CambioPasswordDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.usuario.Configuracion;
import com.cyberpath.springboot.modelo.usuario.Rol;
import com.cyberpath.springboot.modelo.usuario.UltimaConexion;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioMateriaServicio;
import com.cyberpath.springboot.web.jwt.JwtService;
import com.cyberpath.springboot.servicio.servicio.usuario.UsuarioServicio;
import com.cyberpath.springboot.web.login.LoginRequest;
import com.cyberpath.springboot.web.login.LoginResponse;
import com.cyberpath.springboot.web.PasswordManager;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private final UsuarioMateriaServicio usuarioMateriaServicio;
    private final JwtService jwtService;
    private final PasswordManager passwordManager;

    // ============== METODOS PRINCIPALES ===================
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
        try {
            Usuario usuario = mapDtoToEntity(usuarioDto);


            if (usuarioDto.getIdConfiguracion() != null) {
                Configuracion configuracion = Configuracion.builder()
                        .id(usuarioDto.getIdConfiguracion())
                        .build();
                configuracion.setUsuario(usuario);
                usuario.setConfiguracion(configuracion);
            }

            Usuario guardado = usuarioServicio.save(usuario);
            return ResponseEntity.ok(convertToDto(guardado));
        } catch (Exception e) {
            //Log.error("Error al guardar usuario: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
        }

    @PostMapping("/usuario/login/docente")
    public ResponseEntity<?> loginDocente(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioServicio.getByCorreo(request.getCorreo());
        if (usuario == null) { // Usuario con el correo no existe
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado"); // Reporta que el correo no está registrado
        }

        // VALIDAR CONTRASEÑA
        if (!passwordManager.validarContrasena(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getCorreo()); // Genera un Token JWT para manejar el acceso y la comunicación entre el usuario y el servidor

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

        Usuario usuario = new Usuario();
        usuario.setNombreCuenta(newUser.getNombreCuenta());
        usuario.setCorreo(newUser.getCorreo());

        // Contraseña encriptada
        String passwordEncriptada = passwordManager.encode(newUser.getContrasena());
        usuario.setContrasena(passwordEncriptada);

        // Rol asignado desde el front
        if (newUser.getIdRol() != null) {
            usuario.setRol(Rol.builder().id(newUser.getIdRol()).build());
        }

        // Asocia configuracion si hay idConfiguracion
        if (newUser.getIdConfiguracion() != null) {
            Configuracion configuracion = Configuracion.builder()
                    .id(newUser.getIdConfiguracion())
                    .build();
            configuracion.setUsuario(usuario);
            usuario.setConfiguracion(configuracion);
        }

        // Asocia ultimaConexion si hay idUltimaConexion
        if (newUser.getIdUltimaConexion() != null) {
            UltimaConexion ultimaConexion = UltimaConexion.builder()
                    .id(newUser.getIdUltimaConexion())
                    .ultimaConexion(LocalDateTime.now().toString())  // Valor por defecto si no se proporciona
                    .build();
            ultimaConexion.setUsuario(usuario);
            usuario.setUltimaConexion(ultimaConexion);
        }

        Usuario guardado = usuarioServicio.save(usuario);

        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        // Primero verifica que el usuario exista
        Usuario existente = usuarioServicio.getById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario datosActualizacion = mapDtoToEntity(usuarioDto);
        datosActualizacion.setId(id);
        String passwordEncriptada = passwordManager.encode(usuarioDto.getContrasena());
        datosActualizacion.setContrasena(passwordEncriptada);

        // Configuración (opcional)
        if (usuarioDto.getIdConfiguracion() != null) {
            Configuracion configuracion = Configuracion.builder()
                    .id(id) // mismo ID que usuario (porque es 1:1 con MapsId)
                    .build();
            configuracion.setUsuario(existente);
            datosActualizacion.setConfiguracion(configuracion);
        }

        // Última conexión (opcional)
        if (usuarioDto.getIdUltimaConexion() != null) {
            UltimaConexion ultimaConexion = UltimaConexion.builder()
                    .id(id) // mismo ID que usuario
                    .build();
            ultimaConexion.setUsuario(existente);
            datosActualizacion.setUltimaConexion(ultimaConexion);
        }

        Usuario actualizado = usuarioServicio.update(id, datosActualizacion);

        // Ya no puede ser null porque verificamos arriba
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

    @GetMapping("usuario/{id}/materias")
    public ResponseEntity<List<MateriaDto>> getMateriasByUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.getById(id);
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
    /*
    @GetMapping("/usuario/{id}/ejercicios")
    public ResponseEntity<List<EjercicioDto>Long> getEjerciciosByUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.getById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        List<EjercicioDto> ejercicios = usuario.getUsuariosEjercicios()
                .stream()
                .map(ue -> {
                    Ejercicio e = ue.getEjercicio();
                    return EjercicioDto.builder()
                            .id(e.getId())
                            .nombre(e.getNombre())
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ejercicios.size());
    }
     */
    @GetMapping("/usuario/{idUsuario}/materia/{idMateria}/ejercicios-realizados")
    public ResponseEntity<Long> getEjerciciosRealizadosByUsuarioAndMateria(@PathVariable Integer idUsuario, @PathVariable Integer idMateria) {
        Usuario usuario = usuarioServicio.getById(idUsuario);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        // Verifica que la materia exista (opcional, pero recomendado)
        Materia materia = usuarioServicio.getMateriaById(idMateria); // Asume que tienes este método en UsuarioServicio o usa MateriaServicio
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        // Cuenta los ejercicios realizados (hecho = true) para este usuario y materia
        Long cantidad = usuarioServicio.countEjerciciosRealizadosByUsuarioAndMateria(idUsuario, idMateria);
        return ResponseEntity.ok(cantidad);
    }


    // ====================== METODOS AUXILIARES ========================
    @PostMapping("/usuario/login")
    public ResponseEntity<UsuarioDto> login(@RequestBody UsuarioDto loginRequest) {
        if (loginRequest.getNombreCuenta() == null || loginRequest.getContrasena() == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioServicio.findByNombreCuenta(loginRequest.getNombreCuenta());
        if (usuario == null || !usuario.getContrasena().equals(loginRequest.getContrasena())) {
            return ResponseEntity.status(401).build();
        }

        UsuarioDto response = convertToDto(usuario);
        response.setContrasena(null);
        return ResponseEntity.ok(response);
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private UsuarioDto convertToDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombreCuenta(usuario.getNombreCuenta())
                .correo(usuario.getCorreo())
                .contrasena(usuario.getContrasena())
                .nombreCompleto(usuario.getNombreCompleto())
                .activo(usuario.getActivo())
                .verificado(usuario.getVerificado())
                .idRol(usuario.getRol() != null ? usuario.getRol().getId() : null)
                .idConfiguracion(usuario.getConfiguracion() != null ? usuario.getConfiguracion().getId() : null)
                .idUltimaConexion(usuario.getUltimaConexion() != null ? usuario.getUltimaConexion().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
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

