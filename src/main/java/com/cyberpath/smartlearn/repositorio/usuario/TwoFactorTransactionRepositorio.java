package com.cyberpath.smartlearn.repositorio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.TwoFactorTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorTransactionRepositorio extends JpaRepository<TwoFactorTransaction, Long> {
    Optional<TwoFactorTransaction> findByTransactionId(String transactionId);
    Optional<TwoFactorTransaction> findByUsuarioIdAndUsedFalse(Integer usuarioId);
}

