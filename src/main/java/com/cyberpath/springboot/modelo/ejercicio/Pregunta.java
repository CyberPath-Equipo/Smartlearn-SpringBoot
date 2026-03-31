package com.cyberpath.springboot.modelo.ejercicio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_pregunta")
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer id;

    @Lob
    @Column(name = "enunciado", nullable = false)
    private String enunciado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoPregunta tipo;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "puntos", precision = 6, scale = 2)
    private BigDecimal puntos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicio ejercicio;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Opcion> opciones = new ArrayList<>();

    public void addOpcion(Opcion opcion) {
        this.opciones.add(opcion);
        opcion.setPregunta(this);
    }

    public void removeOpcion(Opcion opcion) {
        this.opciones.remove(opcion);
        opcion.setPregunta(null);
    }
}