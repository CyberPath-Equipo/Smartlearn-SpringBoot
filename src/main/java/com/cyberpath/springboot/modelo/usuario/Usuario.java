package com.cyberpath.springboot.modelo.usuario;

import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;
import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
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
@Table(name = "tbl_usuario", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "nombre_cuenta", length = 100, nullable = false, unique = true)
    private String nombreCuenta;

    @Column(name = "correo", length = 255, nullable = false, unique = true)
    private String correo;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "nombre_completo", length = 255)
    private String nombreCompleto;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "verificado")
    @Builder.Default
    private Boolean verificado = false;

    @Column(name = "creado_en", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime creadoEn = LocalDateTime.now();

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Configuracion configuracion;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UltimaConexion ultimaConexion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<IntentoEjercicio> intentoEjercicio = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProgresoSubtema> progresoSubtema = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioMateria> usuariosMaterias = new ArrayList<>();


    public void addIntentoEjercicio(IntentoEjercicio intentoEjercicio) {
        this.intentoEjercicio.add(intentoEjercicio);
        intentoEjercicio.setUsuario(this);
    }

    public void removeIntentoEjercicio(IntentoEjercicio intentoEjercicio) {
        this.intentoEjercicio.remove(intentoEjercicio);
        intentoEjercicio.setUsuario(null);
    }

    public void addProgresoSubtema(ProgresoSubtema progresoSubtema) {
        this.progresoSubtema.add(progresoSubtema);
        progresoSubtema.setUsuario(this);
    }

    public void removeProgresoSubtema(ProgresoSubtema progresoSubtema) {
        this.progresoSubtema.remove(progresoSubtema);
        progresoSubtema.setUsuario(null);
    }

    public void addUsuarioMateria(UsuarioMateria usuarioMateria) {
        this.usuariosMaterias.add(usuarioMateria);
        usuarioMateria.setUsuario(this);
    }

    public void removeUsuarioMateria(UsuarioMateria usuarioMateria) {
        this.usuariosMaterias.remove(usuarioMateria);
        usuarioMateria.setUsuario(null);
    }
}