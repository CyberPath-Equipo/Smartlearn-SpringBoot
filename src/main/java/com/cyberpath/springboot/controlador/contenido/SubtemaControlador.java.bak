package com.cyberpath.springboot.controlador.contenido;

import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.contenido.SubtemaDto;
import com.cyberpath.springboot.dto.contenido.TemaDto;
import com.cyberpath.springboot.dto.contenido.TeoriaDto;
import com.cyberpath.springboot.dto.ejercicio.EjercicioDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.contenido.Tema;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.contenido.SubtemaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class SubtemaControlador {

    private final SubtemaServicio subtemaServicio;

    @GetMapping("/subtema")
    public ResponseEntity<List<SubtemaDto>> lista() {
        List<Subtema> subtemas = subtemaServicio.getAll();
        if (subtemas == null || subtemas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<SubtemaDto> dtos = subtemas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/subtema/{id}")
    public ResponseEntity<SubtemaDto> getById(@PathVariable Integer id) {
        Subtema subtema = subtemaServicio.getById(id);
        if (subtema == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(subtema));
    }

    @GetMapping("/subtema/{id}/tema")
    public ResponseEntity<TemaDto> getTema(@PathVariable Integer id) {
        Subtema subtema = subtemaServicio.getById(id);
        if (subtema == null) {
            return ResponseEntity.notFound().build();
        }

        Tema tema = subtema.getTema();
        if (tema == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                TemaDto.builder()
                        .id(tema.getId())
                        .nombre(tema.getNombre())
                        .build()
        );
    }

    @PostMapping("/subtema")
    public ResponseEntity<SubtemaDto> save(@RequestBody SubtemaDto subtemaDto) {
        Subtema subtema = mapDtoToEntity(subtemaDto);

        // Asocia con Tema si está presente
        if (subtemaDto.getIdTema() != null) {
            subtema.setTema(Tema.builder().id(subtemaDto.getIdTema()).build());
        }

        Subtema guardado = subtemaServicio.save(subtema);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/subtema/{id}")
    public ResponseEntity<SubtemaDto> update(@PathVariable Integer id, @RequestBody SubtemaDto subtemaDto) {
        Subtema datosActualizacion = mapDtoToEntity(subtemaDto);

        if (subtemaDto.getIdTema() != null) {
            datosActualizacion.setTema(Tema.builder().id(subtemaDto.getIdTema()).build());
        }

        Subtema actualizado = subtemaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/subtema/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        subtemaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subtema/{idSubtema}/ejercicios")
    public ResponseEntity<List<EjercicioDto>> getEjerciciosBySubtema(@PathVariable Integer idSubtema) {
        Subtema subtema = subtemaServicio.getById(idSubtema);
        if (subtema == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                subtema.getEjercicios()
                        .stream()
                        .map( s -> EjercicioDto.builder()
                                .id(s.getId())
                                .nombre(s.getNombre())
                                .idSubtema(idSubtema)
                                .hecho(s.getHecho())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private SubtemaDto convertToDto(Subtema subtema) {
        return SubtemaDto.builder()
                .id(subtema.getId())
                .nombre(subtema.getNombre())
                .idTema(subtema.getTema() != null ? subtema.getTema().getId() : null)
                .build();
    }

    private EjercicioDto convertEjercicioToDto(Ejercicio ejercicio) {
        return EjercicioDto.builder()
                .id(ejercicio.getId())
                .nombre(ejercicio.getNombre())
                .hecho(ejercicio.getHecho() != null && ejercicio.getHecho())
                .idSubtema(ejercicio.getSubtema() != null ? ejercicio.getSubtema().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Subtema mapDtoToEntity(SubtemaDto dto) {
        return Subtema.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .build();
    }
}