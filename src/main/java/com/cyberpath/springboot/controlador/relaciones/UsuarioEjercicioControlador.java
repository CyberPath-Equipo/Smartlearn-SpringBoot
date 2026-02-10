package com.cyberpath.springboot.controlador.relaciones;

import com.cyberpath.springboot.dto.relaciones.UsuarioEjercicioDto;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.relaciones.UsuarioEjercicio;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.ejercicio.EjercicioServicio; // Asumiendo que existe un servicio similar para Ejercicio
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioEjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class UsuarioEjercicioControlador {
    private final UsuarioEjercicioServicio usuarioEjercicioServicio;
    private final EjercicioServicio ejercicioServicio; // Cambiado de MateriaServicio a EjercicioServicio

    @GetMapping("/usuario-ejercicio")
    public ResponseEntity<List<UsuarioEjercicioDto>> lista() {
        List<UsuarioEjercicio> usuarioEjercicios = usuarioEjercicioServicio.getAll();
        if (usuarioEjercicios == null || usuarioEjercicios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<UsuarioEjercicioDto> dtos = usuarioEjercicios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/usuario-ejercicio") // Cambiado de /usuario-materia a /usuario-ejercicio
    public ResponseEntity<UsuarioEjercicioDto> save(@RequestBody UsuarioEjercicioDto usuarioEjercicioDto) {
        UsuarioEjercicio usuarioEjercicio = mapDtoToEntity(usuarioEjercicioDto);

        // Asocia con Ejercicio y Usuario si están presentes
        if (usuarioEjercicioDto.getIdEjercicio() != null) {
            usuarioEjercicio.setEjercicio(Ejercicio.builder().id(usuarioEjercicioDto.getIdEjercicio()).build());
        }
        if (usuarioEjercicioDto.getIdUsuario() != null) {
            usuarioEjercicio.setUsuario(Usuario.builder().id(usuarioEjercicioDto.getIdUsuario()).build());
        }

        UsuarioEjercicio guardada = usuarioEjercicioServicio.save(usuarioEjercicio);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/usuario-ejercicio/{id}") // Cambiado de /usuario-materia a /usuario-ejercicio
    public ResponseEntity<UsuarioEjercicioDto> update(@PathVariable Integer id, @RequestBody UsuarioEjercicioDto usuarioEjercicioDto) {
        UsuarioEjercicio datosActualizacion = mapDtoToEntity(usuarioEjercicioDto);

        // Asocia relaciones
        if (usuarioEjercicioDto.getIdEjercicio() != null) {
            datosActualizacion.setEjercicio(Ejercicio.builder().id(usuarioEjercicioDto.getIdEjercicio()).build());
        }
        if (usuarioEjercicioDto.getIdUsuario() != null) {
            datosActualizacion.setUsuario(Usuario.builder().id(usuarioEjercicioDto.getIdUsuario()).build());
        }

        UsuarioEjercicio actualizada = usuarioEjercicioServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/usuario-ejercicio/{id}") // Cambiado de /usuario-materia a /usuario-ejercicio
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioEjercicioServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private UsuarioEjercicioDto convertToDto(UsuarioEjercicio usuarioEjercicio) {
        return UsuarioEjercicioDto.builder()
                .id(usuarioEjercicio.getId())
                .idEjercicio(usuarioEjercicio.getEjercicio() != null ? usuarioEjercicio.getEjercicio().getId() : null) // Cambiado de idMateria a idEjercicio
                .idUsuario(usuarioEjercicio.getUsuario() != null ? usuarioEjercicio.getUsuario().getId() : null)
                .hecho(usuarioEjercicio.getHecho() != null ? usuarioEjercicio.getHecho() : false) // Agregado el campo hecho
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private UsuarioEjercicio mapDtoToEntity(UsuarioEjercicioDto dto) {
        return UsuarioEjercicio.builder()
                .id(dto.getId())
                .hecho(dto.isHecho()) // Agregado el campo hecho
                .build();
    }
}