package com.cyberpath.springboot.controlador.ejercicio;

import com.cyberpath.springboot.dto.ejercicio.EjercicioDto;
import com.cyberpath.springboot.dto.ejercicio.PreguntaDto;
import com.cyberpath.springboot.modelo.contenido.Subtema;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.ejercicio.Pregunta;
import com.cyberpath.springboot.servicio.servicio.ejercicio.EjercicioServicio;
import com.cyberpath.springboot.servicio.servicio.ejercicio.PreguntaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class EjercicioControlador {

    private final EjercicioServicio ejercicioServicio;
    private final PreguntaServicio preguntaServicio;

    @GetMapping("/ejercicio")
    public ResponseEntity<List<EjercicioDto>> lista() {
        List<Ejercicio> ejercicios = ejercicioServicio.getAll();
        if (ejercicios == null || ejercicios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EjercicioDto> dtos = ejercicios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/ejercicio/{id}")
    public ResponseEntity<EjercicioDto> getById(@PathVariable Integer id) {
        Ejercicio ejercicio = ejercicioServicio.getById(id);
        if (ejercicio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(ejercicio));
    }

    @PostMapping("/ejercicio")
    public ResponseEntity<EjercicioDto> save(@RequestBody EjercicioDto ejercicioDto) {
        Ejercicio ejercicio = mapDtoToEntity(ejercicioDto);

        if (ejercicioDto.getIdSubtema() != null) {
            ejercicio.setSubtema(Subtema.builder().id(ejercicioDto.getIdSubtema()).build());
        }

        Ejercicio guardado = ejercicioServicio.save(ejercicio);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PostMapping("/ejercicio/{id}/pregunta")
    public ResponseEntity<PreguntaDto> crearPregunta(@PathVariable Integer id, @RequestBody PreguntaDto preguntaDto) {
        Ejercicio ejercicio = ejercicioServicio.getById(id);
        if (ejercicio == null) {
            return ResponseEntity.notFound().build();
        }

        Pregunta pregunta = Pregunta.builder()
                .enunciado(preguntaDto.getEnunciado())
                .tipo(preguntaDto.getTipo())
                .orden(preguntaDto.getOrden())
                .puntos(preguntaDto.getPuntos())
                .ejercicio(ejercicio)
                .build();

        Pregunta guardado = preguntaServicio.save(pregunta);

        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/ejercicio/{id}")
    public ResponseEntity<EjercicioDto> update(@PathVariable Integer id, @RequestBody EjercicioDto ejercicioDto) {
        Ejercicio datosActualizacion = mapDtoToEntity(ejercicioDto);

        if (ejercicioDto.getIdSubtema() != null) {
            datosActualizacion.setSubtema(Subtema.builder().id(ejercicioDto.getIdSubtema()).build());
        }

        Ejercicio actualizado = ejercicioServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/ejercicio/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ejercicioServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ejercicio/{id}/preguntas")
    public ResponseEntity<List<PreguntaDto>> getPreguntasByEjercicio(@PathVariable Integer id) {
        Ejercicio ejercicio = ejercicioServicio.getById(id);
        if (ejercicio == null) {
            return ResponseEntity.notFound().build();
        }

        List<PreguntaDto> dtos = ejercicio.getPreguntas().stream()
                .map(this::convertPreguntaToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    private PreguntaDto convertPreguntaToDto(Pregunta pregunta) {
        return PreguntaDto.builder()
                .id(pregunta.getId())
                .enunciado(pregunta.getEnunciado())
                .tipo(pregunta.getTipo())
                .orden(pregunta.getOrden())
                .puntos(pregunta.getPuntos())
                .idEjercicio(pregunta.getEjercicio() != null ? pregunta.getEjercicio().getId() : null)
                .build();
    }

    private EjercicioDto convertToDto(Ejercicio ejercicio) {
        return EjercicioDto.builder()
                .id(ejercicio.getId())
                .nombre(ejercicio.getNombre())
                .tipo(ejercicio.getTipo())
                .dificultad(ejercicio.getDificultad())
                .orden(ejercicio.getOrden())
                .activo(ejercicio.getActivo())
                .createdAt(ejercicio.getCreatedAt())
                .idSubtema(ejercicio.getSubtema() != null ? ejercicio.getSubtema().getId() : null)
                .build();
    }

    private PreguntaDto convertToDto(Pregunta pregunta) {
        return PreguntaDto.builder()
                .id(pregunta.getId())
                .enunciado(pregunta.getEnunciado())
                .tipo(pregunta.getTipo())
                .orden(pregunta.getOrden())
                .puntos(pregunta.getPuntos())
                .idEjercicio(pregunta.getEjercicio() != null ? pregunta.getEjercicio().getId() : null)
                .build();
    }

    private Ejercicio mapDtoToEntity(EjercicioDto dto) {
        return Ejercicio.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .tipo(dto.getTipo())
                .dificultad(dto.getDificultad())
                .orden(dto.getOrden())
                .activo(dto.getActivo())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}