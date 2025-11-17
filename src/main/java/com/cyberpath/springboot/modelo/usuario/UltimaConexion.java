package com.cyberpath.springboot.modelo.usuario;

import com.cyberpath.springboot.modelo.contenido.Subtema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_ultima_conexion")
public class UltimaConexion {
    @Id
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "ultima_conexion", nullable = false)
    private LocalDateTime ultimaConexion = LocalDateTime.now();

    @Column(name = "dispositivo",  length = 255)
    private String dispositivo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subtema")
    private Subtema subtema;
}