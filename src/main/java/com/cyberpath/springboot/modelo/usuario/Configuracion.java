package com.cyberpath.springboot.modelo.usuario;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_configuracion")
public class Configuracion {
    @Id
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "modo_audio")
    private boolean modoAudio = false;

    @Column(name = "notificaciones_activadas")
    private boolean notificacionesActivadas = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamano_fuente", nullable = false)
    private TamanoFuente tamanoFuente = TamanoFuente.medio;

    @Column(name = "modo_offline")
    private boolean modoOffline = false;

    @Column(name = "cuenta_creada")
    private boolean cuentaCreada = false;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public enum TamanoFuente {
        pequeno, medio, grande
    }
}