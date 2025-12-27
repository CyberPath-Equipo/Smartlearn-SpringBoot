package com.cyberpath.springboot.controlador.contenido;

import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.contenido.SubtemaDto;
import com.cyberpath.springboot.dto.contenido.TemaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.contenido.Tema;
import com.cyberpath.springboot.servicio.servicio.contenido.TemaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class TemaControlador {

    private final TemaServicio temaServicio;

    @GetMapping("/tema")
    public ResponseEntity<List<TemaDto>> lista() {
        List<Tema> temas = temaServicio.getAll();
        if (temas == null || temas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TemaDto> dtos = temas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tema/{id}")
    public ResponseEntity<TemaDto> getById(@PathVariable Integer id) {
        Tema tema = temaServicio.getById(id);
        if (tema == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(tema));
    }

    @GetMapping("/tema/{id}/subtemas")
    public ResponseEntity<List<SubtemaDto>> getSubtemasByTema(@PathVariable Integer id) {
        Tema tema = temaServicio.getById(id);
        if (tema == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                tema.getSubtemas()
                        .stream()
                        .map(s -> SubtemaDto.builder()
                                .id(s.getId())
                                .nombre(s.getNombre())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/tema/{id}/materia")
    public ResponseEntity<MateriaDto> getMateria(@PathVariable Integer id) {
        Tema tema = temaServicio.getById(id);
        if (tema == null) {
            return ResponseEntity.notFound().build();
        }
        Materia materia = tema.getMateria();
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                MateriaDto.builder()
                        .id(materia.getId())
                        .nombre(materia.getNombre())
                        .descripcion(materia.getDescripcion())
                        .build()
        );
    }

    @PostMapping("/tema")
    public ResponseEntity<TemaDto> save(@RequestBody TemaDto temaDto) {
        Tema tema = mapDtoToEntity(temaDto);

        // Asocia con Materia si está presente
        if (temaDto.getIdMateria() != null) {
            tema.setMateria(Materia.builder().id(temaDto.getIdMateria()).build());
        }

        Tema guardado = temaServicio.save(tema);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/tema/{id}")
    public ResponseEntity<TemaDto> update(@PathVariable Integer id, @RequestBody TemaDto temaDto) {
        Tema datosActualizacion = mapDtoToEntity(temaDto);

        // Asocia con Materia
        if (temaDto.getIdMateria() != null) {
            datosActualizacion.setMateria(Materia.builder().id(temaDto.getIdMateria()).build());
        }

        Tema actualizado = temaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/tema/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        temaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private TemaDto convertToDto(Tema tema) {
        return TemaDto.builder()
                .id(tema.getId())
                .nombre(tema.getNombre())
                .idMateria(tema.getMateria() != null ? tema.getMateria().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Tema mapDtoToEntity(TemaDto dto) {
        return Tema.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .build();
    }
}