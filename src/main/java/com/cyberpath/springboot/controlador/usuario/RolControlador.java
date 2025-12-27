package com.cyberpath.springboot.controlador.usuario;

import com.cyberpath.springboot.dto.usuario.RolDto;
import com.cyberpath.springboot.modelo.usuario.Rol;
import com.cyberpath.springboot.servicio.servicio.usuario.RolServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class RolControlador {

    private final RolServicio rolServicio;

    @GetMapping("/rol")
    public ResponseEntity<List<RolDto>> lista() {
        List<Rol> roles = rolServicio.getAll();
        if (roles == null || roles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<RolDto> dtos = roles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/rol/{id}")
    public ResponseEntity<RolDto> getById(@PathVariable Integer id) {
        Rol rol = rolServicio.getById(id);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(rol));
    }

    @PostMapping("/rol")
    public ResponseEntity<RolDto> save(@RequestBody RolDto rolDto) {
        Rol rol = mapDtoToEntity(rolDto);
        Rol guardado = rolServicio.save(rol);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    @PutMapping("/rol/{id}")
    public ResponseEntity<RolDto> update(@PathVariable Integer id, @RequestBody RolDto rolDto) {
        Rol datosActualizacion = mapDtoToEntity(rolDto);
        Rol actualizado = rolServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizado));
    }

    @DeleteMapping("/rol/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rolServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private RolDto convertToDto(Rol rol) {
        return RolDto.builder()
                .id(rol.getId())
                .tipo(rol.getTipo())
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Rol mapDtoToEntity(RolDto dto) {
        return Rol.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .build();
    }
}