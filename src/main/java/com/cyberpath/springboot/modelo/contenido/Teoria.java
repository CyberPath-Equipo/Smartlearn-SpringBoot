package com.cyberpath.springboot.modelo.contenido;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_teoria")
public class Teoria {
    @Id
    @Column(name = "id_subtema")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_subtema")
    private Subtema subtema;

    @Column(name = "contenido", columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "revisado")
    @Builder.Default
    private Boolean revisado = false;

    @Column(name = "fuente", length = 500)
    private String fuente;

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}