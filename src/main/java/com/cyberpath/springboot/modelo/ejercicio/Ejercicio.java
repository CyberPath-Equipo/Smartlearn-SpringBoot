package com.cyberpath.springboot.modelo.ejercicio;

import com.cyberpath.springboot.modelo.contenido.Subtema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_ejercicio")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    @Builder.Default
    private TipoEjercicio tipo = TipoEjercicio.practica;

    @Column(name = "dificultad")
    @Builder.Default
    private Integer dificultad = 3;

    @Column(name = "orden")
    @Builder.Default
    private Integer orden = 0;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_subtema", nullable = false)
    private Subtema subtema;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pregunta> preguntas = new ArrayList<>();

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<IntentoEjercicio> intentos = new ArrayList<>();

    public enum TipoEjercicio {
        practica, evaluacion, repaso
    }

    public void addPregunta(Pregunta pregunta) {
        this.preguntas.add(pregunta);
        pregunta.setEjercicio(this);
    }

    public void removePregunta(Pregunta pregunta) {
        this.preguntas.remove(pregunta);
        pregunta.setEjercicio(null);
    }

    public void addIntento(IntentoEjercicio intento) {
        this.intentos.add(intento);
        intento.setEjercicio(this);
    }

    public void removeIntento(IntentoEjercicio intento) {
        this.intentos.remove(intento);
        intento.setEjercicio(null);
    }
}