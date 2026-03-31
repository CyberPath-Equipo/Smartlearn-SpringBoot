package com.cyberpath.springboot.controlador.contenido;

import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.contenido.TemaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.servicio.servicio.contenido.MateriaServicio;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MateriaControlador {
    private final MateriaServicio materiaServicio;

    @GetMapping("/materia")
    public ResponseEntity<List<MateriaDto>> lista() {
        List<Materia> materias = materiaServicio.getAll();
        if (materias == null || materias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<MateriaDto> dtos = materias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/materia/{id}")
    public ResponseEntity<MateriaDto> getById(@PathVariable Integer id) {
        Materia materia = materiaServicio.getById(id);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(materia));
    }

    @GetMapping("/materia/{id}/temas")
    public ResponseEntity<List<TemaDto>> getTemasByMateria(@PathVariable Integer id) {
        Materia materia = materiaServicio.getById(id);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                Optional.ofNullable(materia.getTemas())
                        .map(temas -> temas.stream()
                                .map(t -> TemaDto.builder()
                                        .id(t.getId())
                                        .nombre(t.getNombre())
                                        .orden(t.getOrden())
                                        .createdAt(t.getCreatedAt())
                                        .updatedAt(t.getUpdatedAt())
                                        .idMateria(materia.getId())
                                        .build())
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList())
        );
    }

    @PostMapping("/materia")
    public ResponseEntity<MateriaDto> save(@Valid @RequestBody MateriaDto materiaDto) {
        Materia materia = mapDtoToEntity(materiaDto);
        Materia guardada = materiaServicio.save(materia);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/materia/{id}")
    public ResponseEntity<MateriaDto> update(@PathVariable Integer id, @Valid @RequestBody MateriaDto materiaDto) {
        Materia datosActualizacion = mapDtoToEntity(materiaDto);
        Materia actualizada = materiaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/materia/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        materiaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/materia/{id}/total-ejercicios")
    public ResponseEntity<Long> getTotalEjerciciosByMateria(@PathVariable Integer id) {
        Materia materia = materiaServicio.getById(id);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        Long totalEjercicios = materiaServicio.countEjerciciosByMateriaId(id);
        return ResponseEntity.ok(totalEjercicios);
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private MateriaDto convertToDto(Materia materia) {
        return MateriaDto.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .slug(materia.getSlug())
                .descripcion(materia.getDescripcion())
                .createdAt(materia.getCreatedAt())
                .updatedAt(materia.getUpdatedAt())
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Materia mapDtoToEntity(MateriaDto dto) {
        return Materia.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .slug(dto.getSlug())
                .descripcion(dto.getDescripcion())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}