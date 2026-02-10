package com.cyberpath.springboot.modelo.ejercicio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_opcion")
public class Opcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Integer id;

    @Column(name = "texto", nullable = false, length = 255)
    private String texto;

    @Column(name = "es_correcta")
    private Boolean correcta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;
}