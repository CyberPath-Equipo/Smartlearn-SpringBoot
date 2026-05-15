package com.cyberpath.smartlearn.controlador.contenido;

import com.cyberpath.smartlearn.dto.contenido.MateriaDto;
import com.cyberpath.smartlearn.dto.contenido.TemaDto;
import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.servicio.servicio.contenido.MateriaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
                materia.getTemas()
                        .stream()
                        .map(t -> TemaDto.builder()
                                .id(t.getId())
                                .nombre(t.getNombre())
                                .build())
                        .collect(Collectors.toList())
        );
    }


    @PostMapping("/materia")
    public ResponseEntity<MateriaDto> save(@RequestBody MateriaDto materiaDto) {
        Materia materia = mapDtoToEntity(materiaDto);
        Materia guardada = materiaServicio.save(materia);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/materia/{id}")
    public ResponseEntity<MateriaDto> update(@PathVariable Integer id, @RequestBody MateriaDto materiaDto) {
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

    private MateriaDto convertToDto(Materia materia) {
        return MateriaDto.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .slug(materia.getSlug())
                .descripcion(materia.getDescripcion())
                .build();
    }

    private Materia mapDtoToEntity(MateriaDto dto) {
        return Materia.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .slug(dto.getSlug())
                .descripcion(dto.getDescripcion())
                .build();
    }
}