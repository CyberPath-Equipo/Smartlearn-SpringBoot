package com.cyberpath.springboot.modelo.contenido;

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
@Table(name = "tbl_teoria")
public class Teoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teoria")
    private Integer id;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "revisado")
    private Boolean revisado;

    @ManyToOne
    @JoinColumn(name = "id_subtema")
    private Subtema subtema;
}