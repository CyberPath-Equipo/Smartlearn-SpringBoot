USE smartlearn;

-- ============================================================
-- 1. CATÁLOGOS BÁSICOS
-- ============================================================

-- 1. tbl_rol
INSERT INTO tbl_rol (tipo) VALUES
('Estudiante'),
('Profesor'),
('Administrador');

-- 2. tbl_materia
INSERT INTO tbl_materia (nombre, descripcion) VALUES
('Programación en Java', 'Curso completo desde cero hasta POO avanzada');

-- 3. tbl_tema
INSERT INTO tbl_tema (id_materia, nombre) VALUES
(1, 'Fundamentos de POO');

-- 4. tbl_subtema
INSERT INTO tbl_subtema (id_tema, nombre) VALUES
(1, 'Clases y Objetos');

-- 5. tbl_teoria (1:1 con subtema → id_subtema = 1)
INSERT INTO tbl_teoria (id_subtema, contenido, revisado) VALUES
(1, 'La Programación Orientada a Objetos (POO) utiliza clases como plantillas para crear objetos...', 1);

-- 6. tbl_tipo_recurso
INSERT INTO tbl_tipo_recurso (nombre, descripcion) VALUES
('PDF', 'Documento en formato PDF'),
('Video', 'Video explicativo'),
('Enlace', 'Enlace externo'),
('Imagen', 'Diagrama o imagen');

-- ============================================================
-- 2. USUARIOS Y RELACIONES
-- ============================================================

-- 7. tbl_usuario
INSERT INTO tbl_usuario (nombre_cuenta, correo, contrasena, id_rol) VALUES
('7ElIron7', 'iron@example.com', '12345', 1);

-- 8. tbl_usuariomateria (N:M)
INSERT INTO tbl_usuariomateria (id_usuario, id_materia) VALUES
(1, 1);

-- 9. tbl_configuracion (1:1 → id_usuario = 1)
INSERT INTO tbl_configuracion (id_usuario, modo_audio, notificaciones_activadas, tamano_fuente, modo_offline) VALUES
(1, 1, 1, 'grande', 0);

-- 10. tbl_ultima_conexion (1:1 → id_usuario = 1)
INSERT INTO tbl_ultima_conexion (id_usuario, ultima_conexion, dispositivo, id_subtema) VALUES
(1, '2025-11-16 01:30:00', 'Chrome en Windows', 1);

-- ============================================================
-- 3. CONTENIDO EDUCATIVO
-- ============================================================

-- 11. tbl_ejercicio
INSERT INTO tbl_ejercicio (id_subtema, nombre, hecho) VALUES
(1, 'Crear una Clase Básica', 1);

-- 12. tbl_pregunta
INSERT INTO tbl_pregunta (id_ejercicio, enunciado) VALUES
(1, '¿Qué palabra clave define una clase en Java?'),
(1, '¿Cómo se llama el método que crea un objeto?'),
(1, '¿Qué significa encapsulamiento?');

-- 13. tbl_opcion
-- Pregunta 1
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta) VALUES
(1, 'class', 1),
(1, 'object', 0),
(1, 'new', 0),
(1, 'public', 0);

-- Pregunta 2
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta) VALUES
(2, 'constructor', 1),
(2, 'main', 0),
(2, 'void', 0),
(2, 'static', 0);

-- Pregunta 3
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta) VALUES
(3, 'Ocultar datos internos', 1),
(3, 'Heredar de otra clase', 0),
(3, 'Crear múltiples objetos', 0),
(3, 'Ejecutar código rápido', 0);

-- 14. tbl_recurso_adjunto
INSERT INTO tbl_recurso_adjunto (id_subtema, id_tipo_recurso, orden, titulo, url, descripcion) VALUES
(1, 1, 0, 'Introducción a Clases', 'https://example.com/clases.pdf', 'PDF con ejemplos básicos');

-- ============================================================
-- 4. PROGRESO Y ACTIVIDAD
-- ============================================================

-- 15. tbl_intento_ejercicio
INSERT INTO tbl_intento_ejercicio (id_usuario, id_ejercicio, puntaje, fecha) VALUES
(1, 1, 90.00, '2025-11-16 01:35:00');

-- 16. tbl_progreso_subtema
INSERT INTO tbl_progreso_subtema (
  id_usuario, id_subtema,
  teoria_leida, ejercicios_completados, ejercicios_totales, ultimo_acceso
) VALUES
(1, 1, 1, 1, 1, '2025-11-16 01:36:00');