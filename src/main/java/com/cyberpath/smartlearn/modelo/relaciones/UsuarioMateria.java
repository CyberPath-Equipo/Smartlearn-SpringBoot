package com.cyberpath.smartlearn.modelo.relaciones;

import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString(exclude = {"materia", "usuario"})
@AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "tbl_usuario_materia",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_materia"}))
public class UsuarioMateria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}