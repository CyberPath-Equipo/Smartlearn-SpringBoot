package com.cyberpath.springboot.controlador.usuario;

import com.cyberpath.springboot.dto.usuario.UltimaConexionDto;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.usuario.UltimaConexion;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.usuario.UltimaConexionServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class UltimaConexionControlador {

    private final UltimaConexionServicio ultimaConexionServicio;

    @GetMapping("/ultima-conexion")
    public ResponseEntity<List<UltimaConexionDto>> lista() {
        List<UltimaConexion> conexiones = ultimaConexionServicio.getAll();
        if (conexiones == null || conexiones.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<UltimaConexionDto> dtos = conexiones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/ultima-conexion/{id}")
    public ResponseEntity<UltimaConexionDto> getById(@PathVariable Integer id) {
        UltimaConexion conexion = ultimaConexionServicio.getById(id);
        if (conexion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(conexion));
    }

    @PostMapping("/ultima-conexion")
    public ResponseEntity<UltimaConexionDto> save(@RequestBody UltimaConexionDto conexionDto) {
        UltimaConexion conexion = mapDtoToEntity(conexionDto);

        if (conexionDto.getId() != null) {
            conexion.setUsuario(Usuario.builder().id(conexionDto.getId()).build());
        }
        if (conexionDto.getIdSubtema() != null) {
            conexion.setSubtema(Subtema.builder().id(conexionDto.getIdSubtema()).build());
        }

        UltimaConexion guardada = ultimaConexionServicio.save(conexion);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/ultima-conexion/{id}")
    public ResponseEntity<UltimaConexionDto> update(@PathVariable Integer id, @RequestBody UltimaConexionDto conexionDto) {
        UltimaConexion datosActualizacion = mapDtoToEntity(conexionDto);

        datosActualizacion.setUsuario(Usuario.builder().id(id).build());
        if (conexionDto.getIdSubtema() != null) {
            datosActualizacion.setSubtema(Subtema.builder().id(conexionDto.getIdSubtema()).build());
        }

        UltimaConexion actualizada = ultimaConexionServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/ultima-conexion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ultimaConexionServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private UltimaConexionDto convertToDto(UltimaConexion conexion) {
        return UltimaConexionDto.builder()
                .id(conexion.getId())
                .ultimaConexion(conexion.getUltimaConexion())
                .dispositivo(conexion.getDispositivo())
                .idUsuario(conexion.getUsuario() != null ? conexion.getUsuario().getId() : null)
                .idSubtema(conexion.getSubtema() != null ? conexion.getSubtema().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private UltimaConexion mapDtoToEntity(UltimaConexionDto dto) {
        return UltimaConexion.builder()
                .id(dto.getId())
                .ultimaConexion(dto.getUltimaConexion())
                .dispositivo(dto.getDispositivo())
                .build();
    }
}