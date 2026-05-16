package com.cyberpath.smartlearn.controlador.contenido;

import com.cyberpath.smartlearn.dto.contenido.TeoriaDto;
import com.cyberpath.smartlearn.modelo.contenido.Subtema;
import com.cyberpath.smartlearn.modelo.contenido.Teoria;
import com.cyberpath.smartlearn.servicio.servicio.contenido.SubtemaServicio;
import com.cyberpath.smartlearn.servicio.servicio.contenido.TeoriaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class TeoriaControlador {

    private final TeoriaServicio teoriaServicio;
    private final SubtemaServicio subtemaServicio;

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

        if (teoriaDto.getIdSubtema() != null) {
            teoria.setSubtema(Subtema.builder().id(teoriaDto.getIdSubtema()).build());
        }

        Teoria guardada = teoriaServicio.save(teoria);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/teoria/{id}")
    public ResponseEntity<TeoriaDto> update(@PathVariable Integer id, @RequestBody TeoriaDto teoriaDto) {
        Teoria datosActualizacion = mapDtoToEntity(teoriaDto);

        if (teoriaDto.getIdSubtema() != null) {
            Subtema subtema = subtemaServicio.findById(teoriaDto.getIdSubtema());
            datosActualizacion.setSubtema(subtema);
        }

        Teoria actualizada = teoriaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/teoria/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        teoriaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TeoriaDto convertToDto(Teoria teoria) {
        return TeoriaDto.builder()
                .id(teoria.getId())
                .contenido(teoria.getContenido())
                .revisado(teoria.getRevisado())
                .fuente(teoria.getFuente())
                .idSubtema(teoria.getSubtema() != null ? teoria.getSubtema().getId() : null)
                .build();
    }

    private Teoria mapDtoToEntity(TeoriaDto dto) {
        return Teoria.builder()
                .id(dto.getId())
                .contenido(dto.getContenido())
                .revisado(dto.isRevisado())
                .fuente(dto.getFuente())
                .build();
    }

    @PostMapping("/teoria/docente")
    public ResponseEntity<TeoriaDto> saveWeb(@RequestBody TeoriaDto teoriaDto) {
        Teoria teoria = mapDtoToEntity(teoriaDto);
        Subtema subtema = subtemaServicio.getById(teoriaDto.getIdSubtema());

        if (teoriaDto.getIdSubtema() != null) {
            teoria.setSubtema(subtema);
        }

        Teoria guardada = teoriaServicio.save(teoria);
        return ResponseEntity.ok(convertToDto(guardada));
    }
}
