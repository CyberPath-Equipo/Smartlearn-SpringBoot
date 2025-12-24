package com.cyberpath.springboot.controlador.contenido;

import com.cyberpath.springboot.dto.contenido.SubtemaDto;
import com.cyberpath.springboot.dto.contenido.TemaDto;
import com.cyberpath.springboot.dto.contenido.TeoriaDto;
import com.cyberpath.springboot.dto.ejercicio.EjercicioDto;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.contenido.Tema;
import com.cyberpath.springboot.modelo.contenido.Teoria;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.servicio.contenido.SubtemaServicio;
import com.cyberpath.springboot.servicio.ejercicio.EjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SubtemaControlador {

    private final SubtemaServicio subtemaServicio;
    private final EjercicioServicio ejercicioServicio;

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

    @GetMapping("/subtema/{id}/teoria")
    public ResponseEntity<TeoriaDto> getTeoria(@PathVariable Integer id) {
        Subtema subtema = subtemaServicio.getById(id);
        if (subtema == null) {
            return ResponseEntity.notFound().build();
        }

        Teoria teoria = subtema.getTeoria();
        if (teoria == null) {
            return ResponseEntity.noContent().build();
        }
        TeoriaDto dto = TeoriaDto.builder()
                .id(teoria.getId())
                .contenido(teoria.getContenido())
                .revisado(teoria.getRevisado())
                .build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/subtema/{id}/ejercicios")
    public ResponseEntity<List<EjercicioDto>> getEjerciciosBySubtema(@PathVariable Integer id) {

        Subtema subtema = subtemaServicio.getById(id);
        if (subtema == null) {
            return ResponseEntity.notFound().build();
        }

        List<Ejercicio> ejercicios = subtema.getEjercicios();

        List<EjercicioDto> dtos = ejercicios.stream()
                .map(e -> EjercicioDto.builder()
                        .id(e.getId())
                        .nombre(e.getNombre())
                        .hecho(Boolean.TRUE.equals(e.getHecho()))
                        .idSubtema(id)
                        .build()
                )
                .toList();

        return ResponseEntity.ok(dtos);
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

    @PostMapping("/subtema/{id}/ejercicios")
    public ResponseEntity<EjercicioDto> crearEjercicio(@PathVariable Integer id, @RequestBody EjercicioDto dto) {

        Subtema subtema = subtemaServicio.getById(id);
        if (subtema == null) {
            return ResponseEntity.notFound().build();
        }

        Ejercicio ejercicio = Ejercicio.builder()
                .nombre(dto.getNombre())
                .hecho(false)
                .subtema(subtema)
                .build();

        Ejercicio guardado = ejercicioServicio.save(ejercicio);

        return ResponseEntity.ok(convertEjercicioToDto(guardado));
    }

    @PutMapping("/subtema/{id}")
    public ResponseEntity<SubtemaDto> update(@PathVariable Integer id, @RequestBody SubtemaDto subtemaDto) {
        Subtema datosActualizacion = mapDtoToEntity(subtemaDto);

        // Asocia con Tema
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

    @DeleteMapping("/subtema/teoria/{idTeoria}")
    public ResponseEntity<Void> deleteTeoria(@PathVariable Integer idTeoria) {
        subtemaServicio.deleteTeoria(idTeoria);
        return ResponseEntity.noContent().build();
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
                .hecho(ejercicio.getHecho())
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