package com.cyberpath.springboot.modelo.contenido;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "revisado")
    private Boolean revisado;
}