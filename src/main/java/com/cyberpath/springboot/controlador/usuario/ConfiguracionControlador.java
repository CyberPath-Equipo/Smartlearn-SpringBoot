package com.cyberpath.springboot.controlador.usuario;

import com.cyberpath.springboot.dto.usuario.ConfiguracionDto;
import com.cyberpath.springboot.modelo.usuario.Configuracion;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.usuario.ConfiguracionServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@AllArgsConstructor
public class ConfiguracionControlador {

    private final ConfiguracionServicio configuracionServicio;

    @GetMapping("/configuracion")
    public ResponseEntity<List<ConfiguracionDto>> lista() {
        List<Configuracion> configuraciones = configuracionServicio.getAll();
        if (configuraciones == null || configuraciones.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ConfiguracionDto> dtos = configuraciones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/configuracion/{id}")
    public ResponseEntity<ConfiguracionDto> getById(@PathVariable Integer id) {
        Configuracion configuracion = configuracionServicio.getById(id);
        if (configuracion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(configuracion));
    }

    @PostMapping("/configuracion")
    public ResponseEntity<ConfiguracionDto> save(@RequestBody ConfiguracionDto configuracionDto) {
        Configuracion configuracion = mapDtoToEntity(configuracionDto);

        // Asocia con Usuario si el idUsuario está presente
        if (configuracionDto.getId() != null) {
            configuracion.setUsuario(Usuario.builder().id(configuracionDto.getId()).build());
        }

        Configuracion guardada = configuracionServicio.save(configuracion);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/configuracion/{id}")
    public ResponseEntity<ConfiguracionDto> update(@PathVariable Integer id, @RequestBody ConfiguracionDto configuracionDto) {
        Configuracion datosActualizacion = mapDtoToEntity(configuracionDto);

        // Asocia con Usuario usando el id del path
        datosActualizacion.setUsuario(Usuario.builder().id(id).build());

        Configuracion actualizada = configuracionServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/configuracion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        configuracionServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private ConfiguracionDto convertToDto(Configuracion configuracion) {
        return ConfiguracionDto.builder()
                .id(configuracion.getId())
                .cuentaCreada(configuracion.isCuentaCreada())
                .modoAudio(configuracion.isModoAudio())
                .modoOffline(configuracion.isModoOffline())
                .notificacionesActivadas(configuracion.isNotificacionesActivadas())
                .tamanoFuente(configuracion.getTamanoFuente())
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private Configuracion mapDtoToEntity(ConfiguracionDto dto) {
        return Configuracion.builder()
                .id(dto.getId())
                .cuentaCreada(dto.isCuentaCreada())
                .modoAudio(dto.isModoAudio())
                .modoOffline(dto.isModoOffline())
                .notificacionesActivadas(dto.isNotificacionesActivadas())
                .tamanoFuente(dto.getTamanoFuente())
                .build();
    }
}