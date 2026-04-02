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
    @Builder.Default
    private UsuarioMateriaId id = new UsuarioMateriaId();

    @ManyToOne
    @MapsId("idMateria")
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "suscrito_en", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime suscritoEn = LocalDateTime.now();
}