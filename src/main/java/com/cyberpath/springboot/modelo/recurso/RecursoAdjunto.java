package com.cyberpath.springboot.modelo.recurso;

import com.cyberpath.springboot.modelo.contenido.Subtema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_recurso_adjunto")
public class RecursoAdjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Integer id;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_subtema", nullable = false)
    private Subtema subtema;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_recurso", nullable = false)
    private TipoRecurso tipoRecurso;
}