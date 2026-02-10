USE smartlearn;

-- ============================================================
-- 1. CATÁLOGOS BÁSICOS
-- ============================================================

-- Roles
INSERT INTO tbl_rol (tipo) VALUES
('Estudiante'),
('Profesor'),
('Administrador');

-- Tipos de recursos
INSERT INTO tbl_tipo_recurso (nombre, descripcion) VALUES
('PDF', 'Documento en formato PDF'),
('Video', 'Video explicativo'),
('Enlace', 'Enlace externo'),
('Imagen', 'Diagrama o imagen');

-- ============================================================
-- 2. USUARIO DE PRUEBA + CONFIGURACIÓN
-- ============================================================

INSERT INTO tbl_usuario (nombre_cuenta, correo, contrasena, id_rol) VALUES
('7ElIron7', 'iron@example.com', '12345', 1);

INSERT INTO tbl_configuracion (id_usuario, modo_audio, notificaciones_activadas, tamano_fuente, modo_offline)
VALUES (1, 1, 1, 'grande', 0);
SELECT
    s.id_subtema,
    s.nombre AS subtema,
    t.contenido,
    e.id_ejercicio,
    e.nombre AS ejercicio,
    p.id_pregunta,
    p.enunciado
FROM tbl_subtema s
JOIN tbl_teoria t ON s.id_subtema = t.id_subtema
JOIN tbl_ejercicio e ON s.id_subtema = e.id_subtema
JOIN tbl_pregunta p ON e.id_ejercicio = p.id_ejercicio;
