package com.cyberpath.springboot.modelo.contenido;

import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.recurso.RecursoAdjunto;
import com.cyberpath.springboot.modelo.usuario.UltimaConexion;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_subtema")
public class Subtema {
    @Id
    @Column(name = "id_subtema")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tema")
    private Tema tema;

    @OneToMany(mappedBy = "subtema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Teoria> teorias = new ArrayList<>();

    @OneToMany(mappedBy = "subtema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RecursoAdjunto> recursosAdjuntos = new ArrayList<>();

    @OneToMany(mappedBy = "subtema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UltimaConexion> ultimasConexiones = new ArrayList<>();

    @OneToMany(mappedBy = "subtema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProgresoSubtema> progresoSubtemas = new ArrayList<>();

    @OneToMany(mappedBy = "subtema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ejercicio> ejercicios = new ArrayList<>();

    public void addTeoria(Teoria teoria) {
        this.teorias.add(teoria);
        teoria.setSubtema(this);
    }

    public void removeTeoria(Teoria teoria) {
        this.teorias.remove(teoria);
        teoria.setSubtema(null);
    }

    public void addRecursoAdjunto(RecursoAdjunto recursoAdjunto) {
        this.recursosAdjuntos.add(recursoAdjunto);
        recursoAdjunto.setSubtema(this);
    }

    public void removeRecursoAdjunto(RecursoAdjunto recursoAdjunto) {
        this.recursosAdjuntos.remove(recursoAdjunto);
        recursoAdjunto.setSubtema(null);
    }

    public void addUltimaConexion(UltimaConexion ultimaConexion) {
        this.ultimasConexiones.add(ultimaConexion);
        ultimaConexion.setSubtema(this);
    }

    public void removeUltimaConexion(UltimaConexion ultimaConexion) {
        this.ultimasConexiones.remove(ultimaConexion);
        ultimaConexion.setSubtema(null);
    }

    public void addProgresoSubtema(ProgresoSubtema progresoSubtema) {
        this.progresoSubtemas.add(progresoSubtema);
        progresoSubtema.setSubtema(this);
    }

    public void removeProgresoSubtema(ProgresoSubtema progresoSubtema) {
        this.progresoSubtemas.remove(progresoSubtema);
        progresoSubtema.setSubtema(null);
    }

    public void addEjercicio(Ejercicio ejercicio) {
        this.ejercicios.add(ejercicio);
        ejercicio.setSubtema(this);
    }

    public void removeEjercicio(Ejercicio ejercicio) {
        this.ejercicios.remove(ejercicio);
        ejercicio.setSubtema(null);
    }
}