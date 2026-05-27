package com.cyberpath.smartlearn.controlador.usuario;

import com.cyberpath.smartlearn.dto.usuario.ConfiguracionDto;
import com.cyberpath.smartlearn.modelo.usuario.Configuracion;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.servicio.servicio.usuario.ConfiguracionServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<ConfiguracionDto> findById(@PathVariable Integer id) {
        Configuracion configuracion = configuracionServicio.findById(id);
        if (configuracion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(configuracion));
    }

    @PostMapping("/configuracion")
    public ResponseEntity<ConfiguracionDto> save(@RequestBody ConfiguracionDto configuracionDto) {
        Configuracion configuracion = mapDtoToEntity(configuracionDto);

        if (configuracionDto.getIdUsuario() != null) {
            configuracion.setUsuario(Usuario.builder().id(configuracionDto.getIdUsuario()).build());
        }

        Configuracion guardada = configuracionServicio.save(configuracion);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/configuracion/{id}")
    public ResponseEntity<ConfiguracionDto> update(@PathVariable Integer id, @RequestBody ConfiguracionDto configuracionDto) {
        Configuracion datosActualizacion = mapDtoToEntity(configuracionDto);

        datosActualizacion.setUsuario(Usuario.builder().id(id).build());

        Configuracion actualizada = configuracionServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/configuracion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        configuracionServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ConfiguracionDto convertToDto(Configuracion configuracion) {
        return ConfiguracionDto.builder()
                .id(configuracion.getId())
                .cuentaCreada(configuracion.isCuentaCreada())
                .modoAudio(configuracion.isModoAudio())
                .modoOffline(configuracion.isModoOffline())
                .notificacionesActivadas(configuracion.isNotificacionesActivadas())
                .tamanoFuente(configuracion.getTamanoFuente() != null ? configuracion.getTamanoFuente().name() : Configuracion.TamanoFuente.medio.name())
                .idUsuario(configuracion.getUsuario() != null ? configuracion.getUsuario().getId() : null)
                .build();
    }

    private Configuracion mapDtoToEntity(ConfiguracionDto dto) {
        return Configuracion.builder()
                .id(dto.getId())
                .cuentaCreada(dto.isCuentaCreada())
                .modoAudio(dto.isModoAudio())
                .modoOffline(dto.isModoOffline())
                .notificacionesActivadas(dto.isNotificacionesActivadas())
                .tamanoFuente(parseTamanoFuente(dto.getTamanoFuente()))
                .build();
    }

    private Configuracion.TamanoFuente parseTamanoFuente(String tamanoFuente) {
        if (tamanoFuente == null || tamanoFuente.isBlank()) {
            return Configuracion.TamanoFuente.medio;
        }
        try {
            return Configuracion.TamanoFuente.valueOf(tamanoFuente.trim().toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return Configuracion.TamanoFuente.medio;
        }
    }
}