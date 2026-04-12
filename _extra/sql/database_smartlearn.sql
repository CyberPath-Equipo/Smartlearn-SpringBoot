-- smartlearn_schema_mejorado.sql
-- Requiere MySQL 8.0+ (por utf8mb4_0900_ai_ci, GENERATED columns, SIGNAL en triggers)

DROP DATABASE IF EXISTS smartlearn;
CREATE DATABASE smartlearn CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE smartlearn;

-- ============================================================
-- 0. CONVENCIÓN: usar INT UNSIGNED para IDs, timestamps de auditoría,
-- y nombres/constraints consistentes.
-- ============================================================

CREATE TABLE tbl_materia (
  id_materia INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(200) NOT NULL,
  slug VARCHAR(200) NOT NULL,                        -- para URLs/ búsquedas
  descripcion TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_materia),
  UNIQUE KEY uq_materia_slug (slug),
  UNIQUE KEY uq_materia_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_tema (
  id_tema INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_materia INT UNSIGNED NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  orden SMALLINT UNSIGNED DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_tema),
  INDEX idx_tema_materia (id_materia),
  UNIQUE KEY uq_materia_nombre_tema (id_materia, nombre),
  CONSTRAINT fk_tema_materia FOREIGN KEY (id_materia)
    REFERENCES tbl_materia(id_materia) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_subtema (
  id_subtema INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_tema INT UNSIGNED NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  orden SMALLINT UNSIGNED DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_subtema),
  INDEX idx_subtema_tema (id_tema),
  UNIQUE KEY uq_tema_nombre_subtema (id_tema, nombre),
  CONSTRAINT fk_subtema_tema FOREIGN KEY (id_tema)
    REFERENCES tbl_tema(id_tema) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Teoría: 1:1 con subtema (clave primaria compartida)
CREATE TABLE tbl_teoria (
  id_subtema INT UNSIGNED NOT NULL,
  contenido TEXT NOT NULL,
  revisado TINYINT(1) NOT NULL DEFAULT 0,
  fuente VARCHAR(500),
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_subtema),
  CONSTRAINT fk_teoria_subtema FOREIGN KEY (id_subtema)
    REFERENCES tbl_subtema(id_subtema) ON DELETE CASCADE ON UPDATE CASCADE,
  FULLTEXT KEY ft_teoria_contenido (contenido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_tipo_recurso (
  id_tipo_recurso INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id_tipo_recurso),
  UNIQUE KEY uq_tipo_recurso_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_rol (
  id_rol INT UNSIGNED NOT NULL AUTO_INCREMENT,
  tipo VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id_rol),
  UNIQUE KEY uq_rol_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================================
-- 2. CONTENIDO EDUCATIVO
-- ============================================================

CREATE TABLE tbl_ejercicio (
  id_ejercicio INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_subtema INT UNSIGNED NOT NULL,
  nombre VARCHAR(255) NOT NULL,
  tipo ENUM('practica','evaluacion','repaso') DEFAULT 'practica',
  dificultad TINYINT UNSIGNED DEFAULT 3, -- 1-5
  orden SMALLINT UNSIGNED DEFAULT 0,
  activo TINYINT(1) DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_ejercicio),
  INDEX idx_ejercicio_subtema (id_subtema),
  CONSTRAINT fk_ejercicio_subtema FOREIGN KEY (id_subtema)
    REFERENCES tbl_subtema(id_subtema) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_pregunta (
  id_pregunta INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_ejercicio INT UNSIGNED NOT NULL,
  enunciado TEXT NOT NULL,
  tipo ENUM('opcion_multiple','abierta','verdadero_falso') DEFAULT 'opcion_multiple',
  orden SMALLINT UNSIGNED DEFAULT 0,
  puntos DECIMAL(6,2) DEFAULT 1.00,
  PRIMARY KEY (id_pregunta),
  INDEX idx_pregunta_ejercicio (id_ejercicio),
  CONSTRAINT fk_pregunta_ejercicio FOREIGN KEY (id_ejercicio)
    REFERENCES tbl_ejercicio(id_ejercicio) ON DELETE CASCADE ON UPDATE CASCADE,
  FULLTEXT KEY ft_pregunta_enunciado (enunciado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_opcion (
  id_opcion INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_pregunta INT UNSIGNED NOT NULL,
  texto VARCHAR(1000) NOT NULL,
  es_correcta TINYINT(1) NOT NULL DEFAULT 0,
  orden SMALLINT UNSIGNED DEFAULT 0,
  PRIMARY KEY (id_opcion),
  INDEX idx_opcion_pregunta (id_pregunta),
  CONSTRAINT fk_opcion_pregunta FOREIGN KEY (id_pregunta)
    REFERENCES tbl_pregunta(id_pregunta) ON DELETE CASCADE ON UPDATE CASCADE,
  CHECK (es_correcta IN (0,1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_recurso_adjunto (
  id_recurso INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_subtema INT UNSIGNED NOT NULL,
  id_tipo_recurso INT UNSIGNED NOT NULL,
  orden SMALLINT UNSIGNED DEFAULT 0,
  titulo VARCHAR(255) NOT NULL,
  url VARCHAR(1000),
  mime_type VARCHAR(100),
  tamano_bytes BIGINT UNSIGNED,
  descripcion TEXT,
  creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_recurso),
  INDEX idx_recurso_subtema_orden (id_subtema, orden),
  INDEX idx_recurso_tipo (id_tipo_recurso),
  CONSTRAINT fk_recurso_subtema FOREIGN KEY (id_subtema)
    REFERENCES tbl_subtema(id_subtema) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_recurso_tipo FOREIGN KEY (id_tipo_recurso)
    REFERENCES tbl_tipo_recurso(id_tipo_recurso) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================================
-- 3. USUARIOS Y RELACIONES
-- ============================================================

CREATE TABLE tbl_usuario (
  id_usuario INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre_cuenta VARCHAR(100) NOT NULL,
  correo VARCHAR(255) NOT NULL,
  contrasena VARCHAR(255) NOT NULL, -- almacenar hash (bcrypt/argon2) en la capa de aplicación
  nombre_completo VARCHAR(255),
  activo TINYINT(1) NOT NULL DEFAULT 1,
  verificado TINYINT(1) NOT NULL DEFAULT 0,
  creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  id_rol INT UNSIGNED NOT NULL,
  PRIMARY KEY (id_usuario),
  UNIQUE KEY uq_usuario_correo (correo),
  UNIQUE KEY uq_usuario_nombrecuenta (nombre_cuenta),
  INDEX idx_usuario_rol (id_rol),
  CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol)
    REFERENCES tbl_rol(id_rol) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Relación many-to-many: usuario <-> materia
-- usar PK compuesta para evitar id artificial innecesario
CREATE TABLE tbl_usuariomateria (
  id_usuario INT UNSIGNED NOT NULL,
  id_materia INT UNSIGNED NOT NULL,
  suscrito_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario, id_materia),
  INDEX idx_usumat_materia (id_materia),
  CONSTRAINT fk_usumat_usuario FOREIGN KEY (id_usuario)
    REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_usumat_materia FOREIGN KEY (id_materia)
    REFERENCES tbl_materia(id_materia) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================================
-- 4. PROGRESO Y ACTIVIDAD
-- ============================================================

CREATE TABLE tbl_intento_ejercicio (
  id_intento_ejercicio INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_usuario INT UNSIGNED NOT NULL,
  id_ejercicio INT UNSIGNED NOT NULL,
  puntaje DECIMAL(6,2),
  duracion_seg INT UNSIGNED,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado ENUM('completado','en_progreso','abandonado') DEFAULT 'completado',
  PRIMARY KEY (id_intento_ejercicio),
  INDEX idx_intento_usuario_fecha (id_usuario, fecha),
  INDEX idx_intento_ejercicio_puntaje (id_ejercicio, puntaje),
  CONSTRAINT fk_intento_usuario FOREIGN KEY (id_usuario)
    REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_intento_ejercicio FOREIGN KEY (id_ejercicio)
    REFERENCES tbl_ejercicio(id_ejercicio) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_progreso_subtema (
  id_progreso INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_usuario INT UNSIGNED NOT NULL,
  id_subtema INT UNSIGNED NOT NULL,
  teoria_leida TINYINT(1) DEFAULT 0,
  ejercicios_completados INT UNSIGNED DEFAULT 0,
  ejercicios_totales INT UNSIGNED DEFAULT 0,
  porcentaje DECIMAL(5,2) GENERATED ALWAYS AS (
    CASE WHEN ejercicios_totales = 0 THEN 0
         ELSE (ejercicios_completados * 100.0 / ejercicios_totales) END
  ) STORED,
  ultimo_acceso DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_progreso),
  UNIQUE KEY uq_progreso_usuario_subtema (id_usuario, id_subtema),
  INDEX idx_progreso_subtema (id_subtema),
  CONSTRAINT fk_progreso_usuario FOREIGN KEY (id_usuario)
    REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_progreso_subtema FOREIGN KEY (id_subtema)
    REFERENCES tbl_subtema(id_subtema) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_ultima_conexion (
  id_usuario INT UNSIGNED NOT NULL,
  ultima_conexion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dispositivo VARCHAR(255),
  id_subtema INT UNSIGNED,
  PRIMARY KEY (id_usuario),
  INDEX idx_ultimacon_subtema (id_subtema),
  CONSTRAINT fk_ultimacon_usuario FOREIGN KEY (id_usuario)
    REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_ultimacon_subtema FOREIGN KEY (id_subtema)
    REFERENCES tbl_subtema(id_subtema) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================================
-- 5. CONFIGURACIÓN PERSONAL
-- ============================================================

CREATE TABLE tbl_configuracion (
  id_usuario INT UNSIGNED NOT NULL,
  modo_audio TINYINT(1) NOT NULL DEFAULT 0,
  cuenta_creada TINYINT(1) NOT NULL DEFAULT 0,
  notificaciones_activadas TINYINT(1) NOT NULL DEFAULT 1,
  tamano_fuente ENUM('pequeno','medio','grande') NOT NULL DEFAULT 'medio',
  modo_offline TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id_usuario),
  CONSTRAINT fk_config_usuario FOREIGN KEY (id_usuario)
    REFERENCES tbl_usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================================
-- Reglas/Triggers adicionales recomendados
-- 1) Asegurar que por pregunta solo haya UNA opción correcta (en caso de opción múltiple).
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_opcion_before_insert
BEFORE INSERT ON tbl_opcion
FOR EACH ROW
BEGIN
  IF NEW.es_correcta = 1 THEN
    IF (SELECT COUNT(*) FROM tbl_opcion WHERE id_pregunta = NEW.id_pregunta AND es_correcta = 1) >= 1 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe una opción correcta para esa pregunta.';
    END IF;
  END IF;
END$$

CREATE TRIGGER trg_opcion_before_update
BEFORE UPDATE ON tbl_opcion
FOR EACH ROW
BEGIN
  IF NEW.es_correcta = 1 AND NOT (OLD.es_correcta = 1) THEN
    IF (SELECT COUNT(*) FROM tbl_opcion WHERE id_pregunta = NEW.id_pregunta AND es_correcta = 1 AND id_opcion <> OLD.id_opcion) >= 1 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe una opción correcta para esa pregunta.';
    END IF;
  END IF;
END$$

DELIMITER ;

