package com.cyberpath.springboot.controlador.contenido;

import com.cyberpath.springboot.dto.contenido.TeoriaDto;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.contenido.Teoria;
import com.cyberpath.springboot.servicio.servicio.contenido.TeoriaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class TeoriaControlador {

    private final TeoriaServicio teoriaServicio;

    @GetMapping("/teoria")
    public ResponseEntity<List<TeoriaDto>> lista() {
        List<Teoria> teorias = teoriaServicio.getAll();
        if (teorias == null || teorias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TeoriaDto> dtos = teorias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/teoria/{id}")
    public ResponseEntity<TeoriaDto> getById(@PathVariable Integer id) {
        Teoria teoria = teoriaServicio.getById(id);
        if (teoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(teoria));
    }

    @PostMapping("/teoria")
    public ResponseEntity<TeoriaDto> save(@RequestBody TeoriaDto teoriaDto) {
        Teoria teoria = mapDtoToEntity(teoriaDto);

        // Asocia con Subtema si está presente
        if (teoriaDto.getIdSubtema() != null) {
            teoria.setSubtema(Subtema.builder().id(teoriaDto.getIdSubtema()).build());
        }

        Teoria guardada = teoriaServicio.save(teoria);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/teoria/{id}")
    public ResponseEntity<TeoriaDto> update(@PathVariable Integer id, @RequestBody TeoriaDto teoriaDto) {
        Teoria datosActualizacion = mapDtoToEntity(teoriaDto);

        // Asocia con Subtema
        if (teoriaDto.getIdSubtema() != null) {
            datosActualizacion.setSubtema(Subtema.builder().id(teoriaDto.getIdSubtema()).build());
        }

        Teoria actualizada = teoriaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/teoria/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        teoriaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private TeoriaDto convertToDto(Teoria teoria) {
        return TeoriaDto.builder()
                .id(teoria.getId())
                .contenido(teoria.getContenido())
                .revisado(teoria.getRevisado())
                .idSubtema(teoria.getSubtema() != null ? teoria.getSubtema().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Teoria mapDtoToEntity(TeoriaDto dto) {
        return Teoria.builder()
                .id(dto.getId())
                .contenido(dto.getContenido())
                .revisado(dto.isRevisado())
                .build();
    }
}