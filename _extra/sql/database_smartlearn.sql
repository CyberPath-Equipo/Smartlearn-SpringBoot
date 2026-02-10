DROP DATABASE IF EXISTS smartlearn;
CREATE DATABASE smartlearn CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE smartlearn;

-- ============================================================
-- 1. CATÁLOGOS BÁSICOS
-- ============================================================

CREATE TABLE tbl_materia (
  id_materia INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  descripcion TEXT,
  PRIMARY KEY (id_materia)
) ENGINE=InnoDB;

CREATE TABLE tbl_tema (
  id_tema INT NOT NULL AUTO_INCREMENT,
  id_materia INT NOT NULL,
  nombre VARCHAR(255),
  PRIMARY KEY (id_tema),
  INDEX idx_materia (id_materia),
  CONSTRAINT fk_tema_materia
    FOREIGN KEY (id_materia) REFERENCES tbl_materia(id_materia)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_subtema (
  id_subtema INT NOT NULL AUTO_INCREMENT,
  id_tema INT NOT NULL,
  nombre VARCHAR(255),
  PRIMARY KEY (id_subtema),
  INDEX idx_tema (id_tema),
  CONSTRAINT fk_subtema_tema
    FOREIGN KEY (id_tema) REFERENCES tbl_tema(id_tema)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Teoría: 1:1 con subtema (PK compartida)
CREATE TABLE tbl_teoria (
  id_subtema INT NOT NULL,
  contenido TEXT,
  revisado TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_subtema),
  CONSTRAINT fk_teoria_subtema
    FOREIGN KEY (id_subtema) REFERENCES tbl_subtema(id_subtema)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_tipo_recurso (
  id_tipo_recurso INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100),
  descripcion VARCHAR(255),
  PRIMARY KEY (id_tipo_recurso)
) ENGINE=InnoDB;

CREATE TABLE tbl_rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  tipo VARCHAR(100),
  PRIMARY KEY (id_rol)
) ENGINE=InnoDB;

-- ============================================================
-- 2. CONTENIDO EDUCATIVO
-- ============================================================

CREATE TABLE tbl_ejercicio (
  id_ejercicio INT NOT NULL AUTO_INCREMENT,
  id_subtema INT NOT NULL,
  nombre VARCHAR(255),
  hecho TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_ejercicio),
  INDEX idx_subtema (id_subtema),
  CONSTRAINT fk_ejercicio_subtema
    FOREIGN KEY (id_subtema) REFERENCES tbl_subtema(id_subtema)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_pregunta (
  id_pregunta INT NOT NULL AUTO_INCREMENT,
  id_ejercicio INT NOT NULL,
  enunciado TEXT,                    -- quitado NOT NULL (puedes tener preguntas sin texto temporalmente si quieres)
  PRIMARY KEY (id_pregunta),
  INDEX idx_ejercicio (id_ejercicio),
  CONSTRAINT fk_pregunta_ejercicio
    FOREIGN KEY (id_ejercicio) REFERENCES tbl_ejercicio(id_ejercicio)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_opcion (
  id_opcion INT NOT NULL AUTO_INCREMENT,
  id_pregunta INT NOT NULL,
  texto VARCHAR(255),
  es_correcta TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_opcion),
  INDEX idx_pregunta (id_pregunta),
  CONSTRAINT fk_opcion_pregunta
    FOREIGN KEY (id_pregunta) REFERENCES tbl_pregunta(id_pregunta)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_recurso_adjunto (
  id_recurso INT NOT NULL AUTO_INCREMENT,
  id_subtema INT NOT NULL,
  id_tipo_recurso INT NOT NULL,
  orden INT DEFAULT 0,
  titulo VARCHAR(255),
  url VARCHAR(500),                  -- quitado NOT NULL (puedes insertar sin URL mientras la completas)
  descripcion TEXT,
  PRIMARY KEY (id_recurso),
  INDEX idx_subtema_orden (id_subtema, orden),
  INDEX idx_tipo (id_tipo_recurso),
  CONSTRAINT fk_recurso_subtema
    FOREIGN KEY (id_subtema) REFERENCES tbl_subtema(id_subtema)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_recurso_tipo
    FOREIGN KEY (id_tipo_recurso) REFERENCES tbl_tipo_recurso(id_tipo_recurso)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 3. USUARIOS Y RELACIONES
-- ============================================================

CREATE TABLE tbl_usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre_cuenta VARCHAR(100),
  correo VARCHAR(255),
  contrasena VARCHAR(255),
  id_rol INT NOT NULL,               -- dejo NOT NULL porque sin rol el usuario no tiene sentido
  PRIMARY KEY (id_usuario),
  UNIQUE KEY uniq_correo (correo),
  INDEX idx_rol (id_rol),
  CONSTRAINT fk_usuario_rol
    FOREIGN KEY (id_rol) REFERENCES tbl_rol(id_rol)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_usuariomateria (
  id_usuario_materia INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_materia INT NOT NULL,
  PRIMARY KEY (id_usuario_materia),
  UNIQUE KEY uniq_usuario_materia (id_usuario, id_materia),
  INDEX idx_usuario (id_usuario),
  INDEX idx_materia (id_materia),
  CONSTRAINT fk_usumat_usuario
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id_usuario)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_usumat_materia
    FOREIGN KEY (id_materia) REFERENCES tbl_materia(id_materia)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 4. PROGRESO Y ACTIVIDAD
-- ============================================================

CREATE TABLE tbl_intento_ejercicio (
  id_intento_ejercicio INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_ejercicio INT NOT NULL,
  puntaje DECIMAL(5,2),
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_intento_ejercicio),
  INDEX idx_usuario_fecha (id_usuario, fecha DESC),
  INDEX idx_ejercicio_puntaje (id_ejercicio, puntaje DESC),
  CONSTRAINT fk_intento_usuario
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id_usuario)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_intento_ejercicio
    FOREIGN KEY (id_ejercicio) REFERENCES tbl_ejercicio(id_ejercicio)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_progreso_subtema (
  id_progreso INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_subtema INT NOT NULL,
  teoria_leida TINYINT(1) DEFAULT 0,
  ejercicios_completados INT DEFAULT 0,
  ejercicios_totales INT DEFAULT 0,
  porcentaje DECIMAL(5,2) GENERATED ALWAYS AS (
    CASE WHEN ejercicios_totales = 0 THEN 0
         ELSE (ejercicios_completados * 100.0 / ejercicios_totales) END
  ) STORED,
  ultimo_acceso DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_progreso),
  UNIQUE KEY uniq_usuario_subtema (id_usuario, id_subtema),
  INDEX idx_subtema (id_subtema),
  CONSTRAINT fk_progreso_usuario
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id_usuario)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_progreso_subtema
    FOREIGN KEY (id_subtema) REFERENCES tbl_subtema(id_subtema)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tbl_ultima_conexion (
  id_usuario INT NOT NULL,
  ultima_conexion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dispositivo VARCHAR(255),
  id_subtema INT,
  PRIMARY KEY (id_usuario),
  INDEX idx_subtema (id_subtema),
  CONSTRAINT fk_ultimacon_usuario
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id_usuario)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_ultimacon_subtema
    FOREIGN KEY (id_subtema) REFERENCES tbl_subtema(id_subtema)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 5. CONFIGURACIÓN PERSONAL
-- ============================================================

CREATE TABLE tbl_configuracion (
  id_usuario INT NOT NULL,
  modo_audio TINYINT(1) DEFAULT 0,
  cuenta_creada TINYINT(1) DEFAULT 0,
  notificaciones_activadas TINYINT(1) DEFAULT 1,
  tamano_fuente ENUM('pequeno', 'medio', 'grande') DEFAULT 'medio',
  modo_offline TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_usuario),
  CONSTRAINT fk_config_usuario
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id_usuario)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;