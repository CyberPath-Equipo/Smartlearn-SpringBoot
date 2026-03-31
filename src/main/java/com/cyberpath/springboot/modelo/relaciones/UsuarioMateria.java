package com.cyberpath.springboot.modelo.relaciones;

import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_usuariomateria")
public class UsuarioMateria {
    @EmbeddedId
    private UsuarioMateriaId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idMateria")
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "suscrito_en", nullable = false, updatable = false)
    private LocalDateTime suscritoEn;

    public void setMateria(Materia materia) {
        this.materia = materia;
        if (materia != null) {
            if (this.id == null) {
                this.id = new UsuarioMateriaId();
            }
            this.id.setIdMateria(materia.getId());
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            if (this.id == null) {
                this.id = new UsuarioMateriaId();
            }
            this.id.setIdUsuario(usuario.getId());
        }
    }
}