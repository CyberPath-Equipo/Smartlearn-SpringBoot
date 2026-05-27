package com.cyberpath.smartlearn.controlador.ejercicio;

import com.cyberpath.smartlearn.dto.ejercicio.OpcionDto;
import com.cyberpath.smartlearn.modelo.ejercicio.Opcion;
import com.cyberpath.smartlearn.modelo.ejercicio.Pregunta;
import com.cyberpath.smartlearn.servicio.servicio.ejercicio.OpcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<OpcionDto> findById(@PathVariable Integer id) {
        Opcion opcion = opcionServicio.findById(id);
        if (opcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(opcion));
    }

    @PostMapping("/opcion")
    public ResponseEntity<OpcionDto> save(@RequestBody OpcionDto opcionDto) {
        Opcion opcion = mapDtoToEntity(opcionDto);

        if (opcionDto.getIdPregunta() != null) {
            opcion.setPregunta(Pregunta.builder().id(opcionDto.getIdPregunta()).build());
        }

        Opcion guardada = opcionServicio.save(opcion);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/opcion/{id}")
    public ResponseEntity<OpcionDto> update(@PathVariable Integer id, @RequestBody OpcionDto opcionDto) {
        Opcion datosActualizacion = mapDtoToEntity(opcionDto);

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

    private OpcionDto convertToDto(Opcion opcion) {
        return OpcionDto.builder()
                .id(opcion.getId())
                .texto(opcion.getTexto())
                .correcta(opcion.getCorrecta())
                .orden(opcion.getOrden())
                .idPregunta(opcion.getPregunta() != null ? opcion.getPregunta().getId() : null)
                .build();
    }

    private Opcion mapDtoToEntity(OpcionDto dto) {
        return Opcion.builder()
                .id(dto.getId())
                .texto(dto.getTexto())
                .correcta(dto.isCorrecta())
                .orden(dto.getOrden() != null ? dto.getOrden() : 0)
                .build();
    }
}