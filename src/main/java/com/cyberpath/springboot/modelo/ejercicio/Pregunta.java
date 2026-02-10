package com.cyberpath.springboot.modelo.ejercicio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "enunciado", nullable = false, length = 500)
    private String enunciado;

    @ManyToOne(fetch = FetchType.LAZY)
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