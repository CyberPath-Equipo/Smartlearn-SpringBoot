package com.cyberpath.springboot.controlador.ejercicio;

import com.cyberpath.springboot.dto.ejercicio.OpcionDto;
import com.cyberpath.springboot.dto.ejercicio.PreguntaDto;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.ejercicio.Opcion;
import com.cyberpath.springboot.modelo.ejercicio.Pregunta;
import com.cyberpath.springboot.servicio.ejercicio.OpcionServicio;
import com.cyberpath.springboot.servicio.ejercicio.PreguntaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class PreguntaControlador {

    private final PreguntaServicio preguntaServicio;
    private final OpcionServicio opcionServicio;

    @GetMapping("/pregunta")
    public ResponseEntity<List<PreguntaDto>> lista() {
        List<Pregunta> preguntas = preguntaServicio.getAll();
        if (preguntas == null || preguntas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<PreguntaDto> dtos = preguntas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("pregunta/{id}/opciones")
    public ResponseEntity<List<OpcionDto>> listaOpciones(@PathVariable int id) {
        Pregunta pregunta = preguntaServicio.getById(id);
        if (pregunta == null) {
            return ResponseEntity.notFound().build();
        }
        List<Opcion> opciones = pregunta.getOpciones();
        List<OpcionDto> dtos = opciones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/pregunta/{id}")
    public ResponseEntity<PreguntaDto> getById(@PathVariable Integer id) {
        Pregunta pregunta = preguntaServicio.getById(id);
        if (pregunta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(pregunta));
    }

    @PostMapping("/pregunta")
    public ResponseEntity<PreguntaDto> save(@RequestBody PreguntaDto preguntaDto) {
        Pregunta pregunta = mapDtoToEntity(preguntaDto);

        // Asocia con Ejercicio si está presente
        if (preguntaDto.getIdEjercicio() != null) {
            pregunta.setEjercicio(Ejercicio.builder().id(preguntaDto.getIdEjercicio()).build());
        }

        Pregunta guardada = preguntaServicio.save(pregunta);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/pregunta/{id}")
    public ResponseEntity<PreguntaDto> update(@PathVariable Integer id, @RequestBody PreguntaDto preguntaDto) {
        Pregunta datosActualizacion = mapDtoToEntity(preguntaDto);

        // Asocia con Ejercicio
        if (preguntaDto.getIdEjercicio() != null) {
            datosActualizacion.setEjercicio(Ejercicio.builder().id(preguntaDto.getIdEjercicio()).build());
        }

        Pregunta actualizada = preguntaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/pregunta/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        preguntaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private PreguntaDto convertToDto(Pregunta pregunta) {
        return PreguntaDto.builder()
                .id(pregunta.getId())
                .enunciado(pregunta.getEnunciado())
                .idEjercicio(pregunta.getEjercicio() != null ? pregunta.getEjercicio().getId() : null)
                .build();
    }

    private OpcionDto convertToDto(Opcion opcion) {
        return OpcionDto.builder()
                .id(opcion.getId())
                .texto(opcion.getTexto())
                .correcta(opcion.getCorrecta())
                .idPregunta(opcion.getPregunta() != null ? opcion.getPregunta().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Pregunta mapDtoToEntity(PreguntaDto dto) {
        return Pregunta.builder()
                .id(dto.getId())
                .enunciado(dto.getEnunciado())
                .build();
    }
}