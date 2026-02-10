package com.cyberpath.springboot.controlador.contenido;

import com.cyberpath.springboot.dto.contenido.ProgresoSubtemaDto;
import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.contenido.ProgresoSubtemaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class ProgresoSubtemaControlador {

    private final ProgresoSubtemaServicio progresoSubtemaServicio;

    @GetMapping("/progreso-subtema")
    public ResponseEntity<List<ProgresoSubtemaDto>> lista() {
        List<ProgresoSubtema> progresos = progresoSubtemaServicio.getAll();
        if (progresos == null || progresos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ProgresoSubtemaDto> dtos = progresos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/progreso-subtema/{id}")
    public ResponseEntity<ProgresoSubtemaDto> getById(@PathVariable Integer id) {
        ProgresoSubtema progreso = progresoSubtemaServicio.getById(id);
        if (progreso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(progreso));
    }

    @PostMapping("/progreso-subtema")
    public ResponseEntity<ProgresoSubtemaDto> save(@RequestBody ProgresoSubtemaDto progresoDto) {
        ProgresoSubtema progreso = mapDtoToEntity(progresoDto);

        // Asocia con Usuario y Subtema si están presentes
        if (progresoDto.getIdUsuario() != null) {
            progreso.setUsuario(Usuario.builder().id(progresoDto.getIdUsuario()).build());
        }
        if (progresoDto.getIdSubtema() != null) {
            progreso.setSubtema(Subtema.builder().id(progresoDto.getIdSubtema()).build());
        }

        ProgresoSubtema guardado = progresoSubtemaServicio.save(progreso);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/progreso-subtema/{id}")
    public ResponseEntity<ProgresoSubtemaDto> update(@PathVariable Integer id, @RequestBody ProgresoSubtemaDto progresoDto) {
        ProgresoSubtema datosActualizacion = mapDtoToEntity(progresoDto);

        // Asocia relaciones
        if (progresoDto.getIdUsuario() != null) {
            datosActualizacion.setUsuario(Usuario.builder().id(progresoDto.getIdUsuario()).build());
        }
        if (progresoDto.getIdSubtema() != null) {
            datosActualizacion.setSubtema(Subtema.builder().id(progresoDto.getIdSubtema()).build());
        }

        ProgresoSubtema actualizado = progresoSubtemaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/progreso-subtema/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        progresoSubtemaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private ProgresoSubtemaDto convertToDto(ProgresoSubtema progreso) {
        return ProgresoSubtemaDto.builder()
                .id(progreso.getId())
                .teoriaLeida(progreso.getTeoriaLeida())
                .ejerciciosCompletados(progreso.getEjerciciosCompletados())
                .ejerciciosTotales(progreso.getEjerciciosTotales())
                .porcentaje(progreso.getPorcentaje())
                .ultimoAcceso(progreso.getUltimoAcceso())
                .idUsuario(progreso.getUsuario() != null ? progreso.getUsuario().getId() : null)
                .idSubtema(progreso.getSubtema() != null ? progreso.getSubtema().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private ProgresoSubtema mapDtoToEntity(ProgresoSubtemaDto dto) {
        return ProgresoSubtema.builder()
                .id(dto.getId())
                .teoriaLeida(dto.isTeoriaLeida())
                .ejerciciosCompletados(dto.getEjerciciosCompletados())
                .ejerciciosTotales(dto.getEjerciciosTotales())
                .porcentaje(dto.getPorcentaje())
                .ultimoAcceso(dto.getUltimoAcceso())
                .build();
    }
}