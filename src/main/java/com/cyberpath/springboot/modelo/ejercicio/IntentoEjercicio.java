package com.cyberpath.springboot.modelo.ejercicio;

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
@Table(name = "tbl_intento_ejercicio")
public class IntentoEjercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Agregado: ID auto-generado
    @Column(name = "id_intento_ejercicio")
    private Integer id;

    @Column(name = "puntaje", nullable = false)
    private Double puntaje;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_ejercicio")
    private Ejercicio ejercicio;
}