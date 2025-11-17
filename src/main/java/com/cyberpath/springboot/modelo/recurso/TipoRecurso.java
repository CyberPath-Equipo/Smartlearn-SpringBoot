package com.cyberpath.springboot.modelo.recurso;

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
@Table(name = "tbl_tipo_recurso")
public class TipoRecurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_recurso")
    private Integer idTipoRecurso;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "tipoRecurso", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RecursoAdjunto> recursoAdjunto = new ArrayList<>();

    public void addRecursoAdjunto(RecursoAdjunto recursoAdjunto) {
        this.recursoAdjunto.add(recursoAdjunto);
        recursoAdjunto.setTipoRecurso(this);
    }

    public void removeRecursoAdjunto(RecursoAdjunto recursoAdjunto) {
        this.recursoAdjunto.remove(recursoAdjunto);
        recursoAdjunto.setTipoRecurso(null);
    }
}