package com.cyberpath.springboot.modelo.contenido;

import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioMateria> usuariosMaterias = new ArrayList<>();

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) //Relación con Tema
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