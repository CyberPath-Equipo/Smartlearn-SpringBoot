package com.cyberpath.springboot.controlador.recurso;

import com.cyberpath.springboot.dto.recurso.TipoRecursoDto;
import com.cyberpath.springboot.modelo.recurso.TipoRecurso;
import com.cyberpath.springboot.servicio.servicio.recurso.TipoRecursoServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class TipoRecursoControlador {

    private final TipoRecursoServicio tipoRecursoServicio;

    @GetMapping("/tipo-recurso")
    public ResponseEntity<List<TipoRecursoDto>> lista() {
        List<TipoRecurso> tipos = tipoRecursoServicio.getAll();
        if (tipos == null || tipos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TipoRecursoDto> dtos = tipos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tipo-recurso/{id}")
    public ResponseEntity<TipoRecursoDto> getById(@PathVariable Integer id) {
        TipoRecurso tipo = tipoRecursoServicio.getById(id);
        if (tipo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(tipo));
    }

    @PostMapping("/tipo-recurso")
    public ResponseEntity<TipoRecursoDto> save(@RequestBody TipoRecursoDto tipoDto) {
        TipoRecurso tipo = mapDtoToEntity(tipoDto);
        TipoRecurso guardado = tipoRecursoServicio.save(tipo);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/tipo-recurso/{id}")
    public ResponseEntity<TipoRecursoDto> update(@PathVariable Integer id, @RequestBody TipoRecursoDto tipoDto) {
        TipoRecurso datosActualizacion = mapDtoToEntity(tipoDto);
        TipoRecurso actualizado = tipoRecursoServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/tipo-recurso/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoRecursoServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private TipoRecursoDto convertToDto(TipoRecurso tipo) {
        return TipoRecursoDto.builder()
                .id(tipo.getIdTipoRecurso())
                .nombre(tipo.getNombre())
                .descripcion(tipo.getDescripcion())
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private TipoRecurso mapDtoToEntity(TipoRecursoDto dto) {
        return TipoRecurso.builder()
                .idTipoRecurso(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }
}