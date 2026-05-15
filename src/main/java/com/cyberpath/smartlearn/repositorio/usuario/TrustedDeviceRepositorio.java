package com.cyberpath.smartlearn.repositorio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.TrustedDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrustedDeviceRepositorio extends JpaRepository<TrustedDevice, Long> {
    Optional<TrustedDevice> findByDeviceToken(String deviceToken);
    Optional<TrustedDevice> findByUsuarioIdAndDeviceToken(Integer usuarioId, String deviceToken);
    List<TrustedDevice> findByUsuarioIdAndRevokedFalse(Integer usuarioId);
    List<TrustedDevice> findByUsuarioId(Integer usuarioId);
}

