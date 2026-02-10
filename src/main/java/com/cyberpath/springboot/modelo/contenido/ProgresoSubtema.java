package com.cyberpath.springboot.modelo.contenido;

import com.cyberpath.springboot.modelo.usuario.Usuario;
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
@Table(name = "tbl_progreso_subtema")
public class ProgresoSubtema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_progreso")
    private Integer id;

    @Column(name = "teoria_leida")
    private Boolean teoriaLeida;

    @Column(name = "ejercicios_completados", nullable = false)
    private Integer ejerciciosCompletados;

    @Column(name = "ejercicios_totales", nullable = false)
    private Integer ejerciciosTotales;

    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje;

    @Column(name = "ultimo_acceso", nullable = false, length = 50)
    private LocalDateTime ultimoAcceso = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_subtema", nullable = false)
    private Subtema subtema;
}