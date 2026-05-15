package com.cyberpath.smartlearn.modelo.contenido;

import com.cyberpath.smartlearn.modelo.relaciones.UsuarioMateria;
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
@Table(name = "tbl_materia")
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioMateria> usuariosMaterias = new ArrayList<>();

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Tema> temas = new ArrayList<>();

    public void addUsuarioMateria(UsuarioMateria usuarioMateria) {
        this.usuariosMaterias.add(usuarioMateria);
        usuarioMateria.setMateria(this);
    }

    public void removeUsuarioMateria(UsuarioMateria usuarioMateria) {
        this.usuariosMaterias.remove(usuarioMateria);
        usuarioMateria.setMateria(null);
    }

    public void addTema(Tema tema) {
        this.temas.add(tema);
        tema.setMateria(this);
    }

    public void removeTema(Tema tema) {
        this.temas.remove(tema);
        tema.setMateria(null);
    }
}