package com.cyberpath.smartlearn.repositorio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecoveryCodeRepositorio extends JpaRepository<RecoveryCode, Long> {
    List<RecoveryCode> findByUsuarioIdAndUsedFalse(Integer usuarioId);
    List<RecoveryCode> findByUsuarioId(Integer usuarioId);
}

