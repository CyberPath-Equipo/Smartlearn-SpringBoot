package com.cyberpath.springboot.modelo.relaciones;

import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_usuariomateria")
public class UsuarioMateria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_materia")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /* Nota para después
    @PostMapping("/inscribir")
    public ResponseEntity<?> inscribir(@RequestBody InscripcionDTO dto) {
        try {
            usuarioMateriaService.inscribir(dto.getIdUsuario(), dto.getIdMateria());
            return ResponseEntity.ok("Inscrito correctamente");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                .body("El usuario ya está inscrito en esta materia");
        }
    }
     */
}