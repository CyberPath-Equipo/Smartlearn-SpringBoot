package com.cyberpath.springboot.controlador.ejercicio;

import com.cyberpath.springboot.dto.ejercicio.OpcionDto;
import com.cyberpath.springboot.modelo.ejercicio.Opcion;
import com.cyberpath.springboot.modelo.ejercicio.Pregunta;
import com.cyberpath.springboot.servicio.servicio.ejercicio.OpcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class OpcionControlador {

    private final OpcionServicio opcionServicio;

    @GetMapping("/opcion")
    public ResponseEntity<List<OpcionDto>> lista() {
        List<Opcion> opciones = opcionServicio.getAll();
        if (opciones == null || opciones.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<OpcionDto> dtos = opciones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/opcion/{id}")
    public ResponseEntity<OpcionDto> getById(@PathVariable Integer id) {
        Opcion opcion = opcionServicio.getById(id);
        if (opcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(opcion));
    }

    @PostMapping("/opcion")
    public ResponseEntity<OpcionDto> save(@RequestBody OpcionDto opcionDto) {
        Opcion opcion = mapDtoToEntity(opcionDto);

        // Asocia con Pregunta si está presente
        if (opcionDto.getIdPregunta() != null) {
            opcion.setPregunta(Pregunta.builder().id(opcionDto.getIdPregunta()).build());
        }

        Opcion guardada = opcionServicio.save(opcion);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/opcion/{id}")
    public ResponseEntity<OpcionDto> update(@PathVariable Integer id, @RequestBody OpcionDto opcionDto) {
        Opcion datosActualizacion = mapDtoToEntity(opcionDto);

        // Asocia con Pregunta
        if (opcionDto.getIdPregunta() != null) {
            datosActualizacion.setPregunta(Pregunta.builder().id(opcionDto.getIdPregunta()).build());
        }

        Opcion actualizada = opcionServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/opcion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        opcionServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private OpcionDto convertToDto(Opcion opcion) {
        return OpcionDto.builder()
                .id(opcion.getId())
                .texto(opcion.getTexto())
                .correcta(opcion.getCorrecta())
                .idPregunta(opcion.getPregunta() != null ? opcion.getPregunta().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Opcion mapDtoToEntity(OpcionDto dto) {
        return Opcion.builder()
                .id(dto.getId())
                .texto(dto.getTexto())
                .correcta(dto.isCorrecta())
                .build();
    }
}