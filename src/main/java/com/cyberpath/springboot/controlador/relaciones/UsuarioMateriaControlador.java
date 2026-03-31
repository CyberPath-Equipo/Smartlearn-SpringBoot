package com.cyberpath.springboot.controlador.relaciones;

import com.cyberpath.springboot.dto.contenido.MateriaDto;
import com.cyberpath.springboot.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateriaId;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.contenido.MateriaServicio;
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioMateriaServicio;
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

        return ResponseEntity.ok(lista.stream().map(this::convertToDto).collect(Collectors.toList()));
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

        UsuarioMateria guardada = usuarioMateriaServicio.save(usuarioMateria);
        return ResponseEntity.ok(convertToDto(guardada));
    }

    @PutMapping("/usuario-materia/{idUsuario}/{idMateria}")
    public ResponseEntity<UsuarioMateriaDto> update(@PathVariable Integer idUsuario, @PathVariable Integer idMateria,
                                                    @RequestBody UsuarioMateriaDto usuarioMateriaDto) {
        UsuarioMateria datosActualizacion = mapDtoToEntity(usuarioMateriaDto);

        UsuarioMateriaId id = UsuarioMateriaId.builder()
                .idUsuario(idUsuario)
                .idMateria(idMateria)
                .build();

        UsuarioMateria actualizada = usuarioMateriaServicio.update(id, datosActualizacion);
        return ResponseEntity.ok(convertToDto(actualizada));
    }

    @DeleteMapping("/usuario-materia/{idUsuario}/{idMateria}")
    public ResponseEntity<Void> delete(@PathVariable Integer idUsuario, @PathVariable Integer idMateria) {
        UsuarioMateriaId id = UsuarioMateriaId.builder()
                .idUsuario(idUsuario)
                .idMateria(idMateria)
                .build();
        usuarioMateriaServicio.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioMateriaDto convertToDto(UsuarioMateria usuarioMateria) {
        return UsuarioMateriaDto.builder()
                .idMateria(usuarioMateria.getMateria() != null ? usuarioMateria.getMateria().getId() : null)
                .idUsuario(usuarioMateria.getUsuario() != null ? usuarioMateria.getUsuario().getId() : null)
                .suscritoEn(usuarioMateria.getSuscritoEn())
                .build();
    }

    private MateriaDto convertMateriaToDto(Materia materia) {
        return MateriaDto.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .slug(materia.getSlug())
                .descripcion(materia.getDescripcion())
                .createdAt(materia.getCreatedAt())
                .updatedAt(materia.getUpdatedAt())
                .build();
    }

    private UsuarioMateria mapDtoToEntity(UsuarioMateriaDto dto) {
        UsuarioMateria relacion = new UsuarioMateria();
        if (dto.getIdMateria() != null) {
            relacion.setMateria(Materia.builder().id(dto.getIdMateria()).build());
        }
        if (dto.getIdUsuario() != null) {
            relacion.setUsuario(Usuario.builder().id(dto.getIdUsuario()).build());
        }
        relacion.setSuscritoEn(dto.getSuscritoEn());
        return relacion;
    }
}