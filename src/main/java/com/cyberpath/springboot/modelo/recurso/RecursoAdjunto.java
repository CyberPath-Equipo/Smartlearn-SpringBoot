package com.cyberpath.springboot.modelo.recurso;

import com.cyberpath.springboot.modelo.contenido.Subtema;
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
@Table(name = "tbl_recurso_adjunto")
public class RecursoAdjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso_adjunto")
    private Integer id;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "titulo", length = 100)
    private String titulo;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subtema", nullable = false)
    private Subtema subtema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_recurso", nullable = false)
    private TipoRecurso tipoRecurso;
}