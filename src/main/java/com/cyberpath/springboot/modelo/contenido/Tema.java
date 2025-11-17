package com.cyberpath.springboot.modelo.contenido;

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
@Table(name = "tbl_tema")
public class Tema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tema")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia")
    private Materia materia;

    @OneToMany(mappedBy = "tema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Subtema> subtemas = new ArrayList<>();

    public void addSubtema(Subtema subtema) {
        this.subtemas.add(subtema);
        subtema.setTema(this);
    }

    public void removeSubtema(Subtema subtema) {
        this.subtemas.remove(subtema);
        subtema.setTema(null);
    }
}