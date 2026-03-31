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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_subtema")
    private Subtema subtema;

    @Lob
    @Column(name = "contenido", nullable = false)
    private String contenido;

    @Column(name = "revisado", nullable = false)
    private Boolean revisado = false;

    @Column(name = "fuente", length = 500)
    private String fuente;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}