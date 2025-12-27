package com.cyberpath.springboot.controlador.recurso;

import com.cyberpath.springboot.dto.recurso.RecursoAdjuntoDto;
import com.cyberpath.springboot.modelo.recurso.RecursoAdjunto;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.recurso.TipoRecurso;
import com.cyberpath.springboot.servicio.servicio.recurso.RecursoAdjuntoServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class RecursoAdjuntoControlador {

    private final RecursoAdjuntoServicio recursoAdjuntoServicio;

    @GetMapping("/recurso-adjunto")
    public ResponseEntity<List<RecursoAdjuntoDto>> lista() {
        List<RecursoAdjunto> recursos = recursoAdjuntoServicio.getAll();
        if (recursos == null || recursos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<RecursoAdjuntoDto> dtos = recursos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/recurso-adjunto/{id}")
    public ResponseEntity<RecursoAdjuntoDto> getById(@PathVariable Integer id) {
        RecursoAdjunto recurso = recursoAdjuntoServicio.getById(id);
        if (recurso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(recurso));
    }

    @PostMapping("/recurso-adjunto")
    public ResponseEntity<RecursoAdjuntoDto> save(@RequestBody RecursoAdjuntoDto recursoDto) {
        RecursoAdjunto recurso = mapDtoToEntity(recursoDto);

        // Asocia con Subtema y TipoRecurso si están presentes
        if (recursoDto.getIdSubtema() != null) {
            recurso.setSubtema(Subtema.builder().id(recursoDto.getIdSubtema()).build());
        }
        if (recursoDto.getIdTipoRecurso() != null) {
            recurso.setTipoRecurso(TipoRecurso.builder().idTipoRecurso(recursoDto.getIdTipoRecurso()).build());
        }

        RecursoAdjunto guardado = recursoAdjuntoServicio.save(recurso);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/recurso-adjunto/{id}")
    public ResponseEntity<RecursoAdjuntoDto> update(@PathVariable Integer id, @RequestBody RecursoAdjuntoDto recursoDto) {
        RecursoAdjunto datosActualizacion = mapDtoToEntity(recursoDto);

        // Asocia relaciones
        if (recursoDto.getIdSubtema() != null) {
            datosActualizacion.setSubtema(Subtema.builder().id(recursoDto.getIdSubtema()).build());
        }
        if (recursoDto.getIdTipoRecurso() != null) {
            datosActualizacion.setTipoRecurso(TipoRecurso.builder().idTipoRecurso(recursoDto.getIdTipoRecurso()).build());
        }

        RecursoAdjunto actualizado = recursoAdjuntoServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/recurso-adjunto/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        recursoAdjuntoServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private RecursoAdjuntoDto convertToDto(RecursoAdjunto recurso) {
        return RecursoAdjuntoDto.builder()
                .id(recurso.getId())
                .orden(recurso.getOrden())
                .titulo(recurso.getTitulo())
                .url(recurso.getUrl())
                .descripcion(recurso.getDescripcion())
                .idSubtema(recurso.getSubtema() != null ? recurso.getSubtema().getId() : null)
                .idTipoRecurso(recurso.getTipoRecurso() != null ? recurso.getTipoRecurso().getIdTipoRecurso() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private RecursoAdjunto mapDtoToEntity(RecursoAdjuntoDto dto) {
        return RecursoAdjunto.builder()
                .id(dto.getId())
                .orden(dto.getOrden())
                .titulo(dto.getTitulo())
                .url(dto.getUrl())
                .descripcion(dto.getDescripcion())
                .build();
    }
}