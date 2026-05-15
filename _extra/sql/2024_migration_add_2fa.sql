-- Script de migración para agregar 2FA a la base de datos SmartLearn
-- Ejecutar en orden

-- 1. Agregar columnas de 2FA a la tabla usuario
ALTER TABLE tbl_usuario
ADD COLUMN two_factor_enabled BOOLEAN DEFAULT FALSE,
ADD COLUMN two_factor_type VARCHAR(10),
ADD COLUMN two_factor_secret LONGTEXT;

-- 2. Crear tabla para transacciones de 2FA
CREATE TABLE tbl_two_factor_transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  transaction_id VARCHAR(64) NOT NULL UNIQUE,
  user_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  channel VARCHAR(10),
  sms_code_hash TEXT,
  used BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (user_id) REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE,
  INDEX idx_transaction_id (transaction_id),
  INDEX idx_user_id (user_id)
);

-- 3. Crear tabla para dispositivos confiables
CREATE TABLE tbl_trusted_device (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  device_token VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP,
  device_info TEXT,
  revoked BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (user_id) REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_device_token (device_token)
);

-- 4. Crear tabla para códigos de recuperación
CREATE TABLE tbl_recovery_code (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  code_hash VARCHAR(255) NOT NULL,
  used BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE,
  INDEX idx_user_id (user_id)
);

-- Índices adicionales para optimizar búsquedas
CREATE INDEX idx_two_factor_enabled ON tbl_usuario(two_factor_enabled);
CREATE INDEX idx_tft_created_at ON tbl_two_factor_transaction(created_at);
CREATE INDEX idx_trusted_device_revoked ON tbl_trusted_device(revoked);
CREATE INDEX idx_recovery_code_used ON tbl_recovery_code(used);

