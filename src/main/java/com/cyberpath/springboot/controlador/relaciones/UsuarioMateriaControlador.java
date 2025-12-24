package com.cyberpath.springboot.controlador.relaciones;

import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import com.cyberpath.springboot.servicio.contenido.MateriaServicio;
import com.cyberpath.springboot.servicio.relaciones.UsuarioMateriaServicio;
import com.cyberpath.springboot.servicio.usuario.UsuarioServicio;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/smartlearn/api")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UsuarioMateriaControlador {

    private final UsuarioMateriaServicio usuarioMateriaServicio;
    private final MateriaServicio materiaServicio;
    private final UsuarioServicio usuarioServicio;

    @GetMapping("/usuario-materia")
    public ResponseEntity<List<UsuarioMateriaDto>> lista() {
        List<UsuarioMateria> usuarioMaterias = usuarioMateriaServicio.getAll();
        if (usuarioMaterias == null || usuarioMaterias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<UsuarioMateriaDto> dtos = usuarioMaterias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario-materia/materia/{idMateria}/usuarios")
    public ResponseEntity<List<UsuarioMateriaDto>> getUsuariosByMateria(@PathVariable Integer idMateria) {
        Materia materia = materiaServicio.getById(idMateria);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        List<UsuarioMateria> lista = materia.getUsuariosMaterias();
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                lista.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/usuario-materia/usuario/{idUsuario}/materias")
    public ResponseEntity<List<MateriaDto>> getMateriasByUsuario(@PathVariable Integer idUsuario) {

        List<Materia> materias = usuarioMateriaServicio.getMateriasByUser(idUsuario);

        if (materias == null || materias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                materias.stream()
                        .map(this::convertMateriaToDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/usuario-materia")
    public ResponseEntity<UsuarioMateriaDto> save(@RequestBody UsuarioMateriaDto usuarioMateriaDto) {
        UsuarioMateria usuarioMateria = mapDtoToEntity(usuarioMateriaDto);
        Materia materia =  materiaServicio.getById(usuarioMateriaDto.getIdMateria());
        Usuario usuario = usuarioServicio.getById(usuarioMateriaDto.getIdUsuario());

        // Asocia con Materia y Usuario si están presentes
        if (usuarioMateriaDto.getIdMateria() != null) {
            usuarioMateria.setMateria(materia);
        }
        if (usuarioMateriaDto.getIdUsuario() != null) {
            usuarioMateria.setUsuario(usuario);
        }

        UsuarioMateria guardada = usuarioMateriaServicio.save(usuarioMateria);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/usuario-materia/{id}")
    public ResponseEntity<UsuarioMateriaDto> update(@PathVariable Integer id, @RequestBody UsuarioMateriaDto usuarioMateriaDto) {
        UsuarioMateria datosActualizacion = mapDtoToEntity(usuarioMateriaDto);

        // Asocia relaciones
        if (usuarioMateriaDto.getIdMateria() != null) {
            datosActualizacion.setMateria(Materia.builder().id(usuarioMateriaDto.getIdMateria()).build());
        }
        if (usuarioMateriaDto.getIdUsuario() != null) {
            datosActualizacion.setUsuario(Usuario.builder().id(usuarioMateriaDto.getIdUsuario()).build());
        }

        UsuarioMateria actualizada = usuarioMateriaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/usuario-materia/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioMateriaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== MÉTODOS DE CONVERSIÓN ======================
    private UsuarioMateriaDto convertToDto(UsuarioMateria usuarioMateria) {
        return UsuarioMateriaDto.builder()
                .id(usuarioMateria.getId())
                .idMateria(usuarioMateria.getMateria() != null ? usuarioMateria.getMateria().getId() : null)
                .idUsuario(usuarioMateria.getUsuario() != null ? usuarioMateria.getUsuario().getId() : null)
                .build();
    }

    private MateriaDto convertMateriaToDto(Materia materia) {
        return MateriaDto.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .descripcion(materia.getDescripcion())
                .build();
    }

    // ====================== MAPEO DTO → ENTIDAD ======================
    private UsuarioMateria mapDtoToEntity(UsuarioMateriaDto dto) {
        return UsuarioMateria.builder()
                .id(dto.getId())
                .build();
    }
}