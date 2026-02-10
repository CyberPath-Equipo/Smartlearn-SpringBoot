package com.cyberpath.springboot.controlador.ejercicio;

import com.cyberpath.springboot.dto.ejercicio.IntentoEjercicioDto;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.ejercicio.IntentoEjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class IntentoEjercicioControlador {

    private final IntentoEjercicioServicio intentoEjercicioServicio;

    @GetMapping("/intento-ejercicio")
    public ResponseEntity<List<IntentoEjercicioDto>> lista() {
        List<IntentoEjercicio> intentos = intentoEjercicioServicio.getAll();
        if (intentos == null || intentos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<IntentoEjercicioDto> dtos = intentos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/intento-ejercicio/{id}")
    public ResponseEntity<IntentoEjercicioDto> getById(@PathVariable Integer id) {
        IntentoEjercicio intento = intentoEjercicioServicio.getById(id);
        if (intento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(intento));
    }

    @PostMapping("/intento-ejercicio")
    public ResponseEntity<IntentoEjercicioDto> save(@RequestBody IntentoEjercicioDto intentoDto) {
        IntentoEjercicio intento = mapDtoToEntity(intentoDto);

        if (intentoDto.getIdUsuario() != null) {
            intento.setUsuario(Usuario.builder().id(intentoDto.getIdUsuario()).build());
        }
        if (intentoDto.getIdEjercicio() != null) {
            intento.setEjercicio(Ejercicio.builder().id(intentoDto.getIdEjercicio()).build());
        }

        IntentoEjercicio guardado = intentoEjercicioServicio.save(intento);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/intento-ejercicio/{id}")
    public ResponseEntity<IntentoEjercicioDto> update(@PathVariable Integer id, @RequestBody IntentoEjercicioDto intentoDto) {
        IntentoEjercicio datosActualizacion = mapDtoToEntity(intentoDto);

        if (intentoDto.getIdUsuario() != null) {
            datosActualizacion.setUsuario(Usuario.builder().id(intentoDto.getIdUsuario()).build());
        }
        if (intentoDto.getIdEjercicio() != null) {
            datosActualizacion.setEjercicio(Ejercicio.builder().id(intentoDto.getIdEjercicio()).build());
        }

        IntentoEjercicio actualizado = intentoEjercicioServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/intento-ejercicio/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        intentoEjercicioServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private IntentoEjercicioDto convertToDto(IntentoEjercicio intento) {
        return IntentoEjercicioDto.builder()
                .id(intento.getId())
                .puntaje(intento.getPuntaje())
                .fecha(intento.getFecha())
                .idUsuario(intento.getUsuario() != null ? intento.getUsuario().getId() : null)
                .idEjercicio(intento.getEjercicio() != null ? intento.getEjercicio().getId() : null)
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private IntentoEjercicio mapDtoToEntity(IntentoEjercicioDto dto) {
        return IntentoEjercicio.builder()
                .id(dto.getId())
                .puntaje(dto.getPuntaje())
                .fecha(dto.getFecha())
                .build();
    }
}