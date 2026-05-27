package com.cyberpath.smartlearn.controlador.usuario;

import com.cyberpath.smartlearn.dto.usuario.UltimaConexionDto;
import com.cyberpath.smartlearn.modelo.contenido.Subtema;
import com.cyberpath.smartlearn.modelo.usuario.UltimaConexion;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UltimaConexionServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class UltimaConexionControlador {

    private final UltimaConexionServicio ultimaConexionServicio;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    public ResponseEntity<UltimaConexionDto> findById(@PathVariable Integer id) {
        UltimaConexion conexion = ultimaConexionServicio.findById(id);
        if (conexion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(conexion));
    }

    @PostMapping("/ultima-conexion")
    public ResponseEntity<UltimaConexionDto> save(@RequestBody UltimaConexionDto conexionDto) {
        try {
            UltimaConexion conexion = mapDtoToEntity(conexionDto);

            if (conexionDto.getIdUsuario() != null) {
                conexion.setUsuario(Usuario.builder().id(conexionDto.getIdUsuario()).build());
            }
            if (conexionDto.getIdSubtema() != null) {
                conexion.setSubtema(Subtema.builder().id(conexionDto.getIdSubtema()).build());
            }

            UltimaConexion guardada = ultimaConexionServicio.save(conexion);
            return ResponseEntity.ok(convertToDto(guardada));
        } catch (Exception e) {
            System.err.println("ERROR 500 en save: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/ultima-conexion/{id}")
    public ResponseEntity<UltimaConexionDto> update(@PathVariable Integer id, @RequestBody UltimaConexionDto conexionDto) {
        try {
            UltimaConexion datosActualizacion = mapDtoToEntity(conexionDto);

            datosActualizacion.setUsuario(Usuario.builder().id(id).build());

            if (conexionDto.getIdSubtema() != null) {
                datosActualizacion.setSubtema(Subtema.builder().id(conexionDto.getIdSubtema()).build());
            }

            UltimaConexion actualizada = ultimaConexionServicio.update(id, datosActualizacion);
            return ResponseEntity.ok(convertToDto(actualizada));
        } catch (Exception e) {
            System.err.println("ERROR 500 en update: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/ultima-conexion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ultimaConexionServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UltimaConexionDto convertToDto(UltimaConexion conexion) {
        return UltimaConexionDto.builder()
                .id(conexion.getId())
                .ultimaConexion(conexion.getUltimaConexion())
                .dispositivo(conexion.getDispositivo())
                .idUsuario(conexion.getUsuario() != null ? conexion.getUsuario().getId() : null)
                .idSubtema(conexion.getSubtema() != null ? conexion.getSubtema().getId() : null)
                .build();
    }

    private UltimaConexion mapDtoToEntity(UltimaConexionDto dto) {
        String fechaStr = dto.getUltimaConexion();

        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            fechaStr = LocalDateTime.now().format(FORMATTER);
        }

        try {
            LocalDateTime.parse(fechaStr, FORMATTER);
        } catch (Exception e) {
            System.err.println("Fecha inválida recibida: " + fechaStr + " - Usando fecha actual");
            fechaStr = LocalDateTime.now().format(FORMATTER);
        }

        return UltimaConexion.builder()
                .id(dto.getId())
                .ultimaConexion(fechaStr)
                .dispositivo(dto.getDispositivo())
                .build();
    }
}