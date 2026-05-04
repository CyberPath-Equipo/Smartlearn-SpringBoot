USE smartlearn;

-- ============================================================
-- 0. LIMPIAR DATOS PREVIOS (Opcional)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE tbl_configuracion;
TRUNCATE TABLE tbl_ultima_conexion;
TRUNCATE TABLE tbl_progreso_subtema;
TRUNCATE TABLE tbl_intento_ejercicio;
TRUNCATE TABLE tbl_usuario_materia;
TRUNCATE TABLE tbl_usuario;
TRUNCATE TABLE tbl_recurso_adjunto;
TRUNCATE TABLE tbl_opcion;
TRUNCATE TABLE tbl_pregunta;
TRUNCATE TABLE tbl_ejercicio;
TRUNCATE TABLE tbl_teoria;
TRUNCATE TABLE tbl_subtema;
TRUNCATE TABLE tbl_tema;
TRUNCATE TABLE tbl_materia;
TRUNCATE TABLE tbl_tipo_recurso;
TRUNCATE TABLE tbl_rol;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. CATÁLOGOS BÁSICOS
-- ============================================================

-- Roles
INSERT INTO tbl_rol (tipo, descripcion) VALUES
('ESTUDIANTE', 'Usuario que estudia las materias'),
('PROFESOR', 'Crea y gestiona contenido'),
('ADMIN', 'Gestión completa del sistema');

-- Tipos de recursos
INSERT INTO tbl_tipo_recurso (nombre, descripcion) VALUES
('PDF', 'Documento en formato PDF descargable'),
('Video', 'Video explicativo de YouTube/Vimeo'),
('Enlace', 'Enlace externo a recurso educativo'),
('Imagen', 'Diagrama, infografía o imagen explicativa'),
('Audio', 'Podcast o audio explicativo');

-- ============================================================
-- 2. MATERIAS
-- ============================================================

INSERT INTO tbl_materia (nombre, slug, descripcion) VALUES
('Matemáticas Básicas', 'matematicas-basicas', 'Conceptos fundamentales de matemáticas'),
('Álgebra', 'algebra', 'Operaciones algebraicas y ecuaciones'),
('Geometría', 'geometria', 'Figuras geométricas y teoremas'),
('Español', 'espanol', 'Gramática y literatura española');

-- ============================================================
-- 3. TEMAS POR MATERIA
-- ============================================================

-- Matemáticas Básicas
INSERT INTO tbl_tema (id_materia, nombre, orden) VALUES
(1, 'Números Naturales', 1),
(1, 'Operaciones Básicas', 2),
(1, 'Fracciones', 3);

-- Álgebra
INSERT INTO tbl_tema (id_materia, nombre, orden) VALUES
(2, 'Expresiones Algebraicas', 1),
(2, 'Ecuaciones Lineales', 2);

-- Geometría
INSERT INTO tbl_tema (id_materia, nombre, orden) VALUES
(3, 'Figuras Planas', 1),
(3, 'Áreas y Perímetros', 2);

-- Español
INSERT INTO tbl_tema (id_materia, nombre, orden) VALUES
(4, 'Ortografía', 1),
(4, 'Gramática', 2);

-- ============================================================
-- 4. SUBTEMAS
-- ============================================================

-- Números Naturales (Tema ID=1)
INSERT INTO tbl_subtema (id_tema, nombre, orden) VALUES
(1, 'Clasificación de números', 1),
(1, 'Valor posicional', 2);

-- Operaciones Básicas (Tema ID=2)
INSERT INTO tbl_subtema (id_tema, nombre, orden) VALUES
(2, 'Suma y resta', 1),
(2, 'Multiplicación', 2),
(2, 'División', 3);

-- Expresiones Algebraicas (Tema ID=4)
INSERT INTO tbl_subtema (id_tema, nombre, orden) VALUES
(4, 'Variables y constantes', 1),
(4, 'Simplificación', 2);

-- ============================================================
-- 5. TEORÍA (1:1 con Subtema)
-- ============================================================

INSERT INTO tbl_teoria (id_subtema, contenido, revisado, fuente) VALUES
-- Clasificación de números
(1, 'Los números naturales se clasifican en: pares, impares, primos y compuestos. Un número primo solo es divisible por 1 y por sí mismo.', 1, 'Libro de texto 1º primaria'),

-- Valor posicional
(2, 'En el número 1234: 4=unidades, 3=decenas, 2=centenas, 1=millares.', 1, 'https://ejemplo.com/valor-posicional'),

-- Suma y resta
(3, 'bienvenido curso asiento', 1, 'Khan Academy'),

-- Multiplicación
(4, 'Multiplicación: 12 × 34 = 408. Método de multiplicación por filas.', 1, NULL),

-- Variables y constantes
(7, 'Variable: x, y (cambian). Constante: 5, 10 (fijas). Ejemplo: 2x + 3 = 7', 1, 'Álgebra básica');

-- ============================================================
-- 6. EJERCICIOS
-- ============================================================

INSERT INTO tbl_ejercicio (id_subtema, nombre, tipo, dificultad, orden) VALUES
-- Suma y resta
(3, 'Ejercicio de suma básica', 'practica', 1, 1),
(3, 'Ejercicio de resta', 'practica', 2, 2),
(3, 'Evaluación suma/resta', 'evaluacion', 3, 3),

-- Multiplicación
(4, 'Tablas de multiplicar', 'practica', 2, 1),
(4, 'Multiplicación de 2 dígitos', 'evaluacion', 4, 2);

-- ============================================================
-- PREGUNTAS
-- ============================================================

-- Ejercicio suma básica (ID=1)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos) VALUES
(1, '¿Cuánto es 15 + 27?', 'opcion_multiple', 1, 1.00),
(1, 'Calcula: 48 - 23', 'opcion_multiple', 2, 1.00);

-- Ejercicio tablas (ID=4)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos) VALUES
(4, '¿Cuánto es 7 × 8?', 'opcion_multiple', 1, 1.00);

-- ============================================================
-- OPCIONES
-- ============================================================

-- Pregunta 15+27 (ID=1)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES
(1, '42', 1, 1),
(1, '32', 0, 2),
(1, '52', 0, 3);

-- Pregunta 48-23 (ID=2)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES
(2, '25', 1, 1),
(2, '71', 0, 2),
(2, '35', 0, 3);

-- Pregunta 7×8 (ID=3)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES
(3, '56', 1, 1),
(3, '48', 0, 2),
(3, '65', 0, 3);

INSERT INTO tbl_recurso_adjunto (id_subtema, id_tipo_recurso, orden, titulo, url, mime_type, tamano_bytes, descripcion) VALUES
-- Suma y resta
(3, 1, 1, 'Ficha de sumas', 'https://ejemplo.com/sumas.pdf', 'application/pdf', 1024000, 'Fichas imprimibles'),
(3, 2, 2, 'Video sumas', 'https://youtube.com/watch?v=123', 'video/youtube', NULL, 'Video explicativo'),

-- Multiplicación
(4, 4, 1, 'Tabla del 7', 'https://ejemplo.com/tabla7.png', 'image/png', 25000, 'Imagen tabla multiplicar');

SELECT 'Datos cargados correctamente' AS mensaje;
SELECT COUNT(*) AS total_materias FROM tbl_materia;
SELECT COUNT(*) AS total_temas FROM tbl_tema;
SELECT COUNT(*) AS total_subtemas FROM tbl_subtema;
SELECT COUNT(*) AS total_ejercicios FROM tbl_ejercicio;
SELECT COUNT(*) AS total_preguntas FROM tbl_pregunta;
SELECT COUNT(*) AS total_usuarios FROM tbl_usuario;

-- Paso 1: Agregar tema Literatura (ID=6 automático)
INSERT INTO tbl_tema (id_materia, nombre, orden) VALUES (4, 'Literatura', 3);
SET @id_tema_literatura = LAST_INSERT_ID();

-- Paso 2: Agregar subtema Poemas (ID automático)
INSERT INTO tbl_subtema (id_tema, nombre, orden) VALUES (@id_tema_literatura, 'Poemas en español', 1);
SET @id_subtema_poemas = LAST_INSERT_ID();

-- Paso 3: Agregar teoría (USANDO ID REAL del subtema)
INSERT INTO tbl_teoria (id_subtema, contenido, revisado, fuente) VALUES
(@id_subtema_poemas, 'Los poemas son textos literarios que usan ritmo, rima y figuras literarias. Ejemplo sencillo:\n\n"Estrellita dónde estás\nMe pregunto qué serás\nEn el cielo y en el mar\nUn diamante de verdad."\n\nEste poema tiene rima (estás/serás) y ritmo.', 1, 'Poesía infantil básica');

-- Paso 4: Agregar ejercicio
INSERT INTO tbl_ejercicio (id_subtema, nombre, tipo, dificultad, orden) VALUES
(@id_subtema_poemas, 'Identifica la rima en el poema', 'practica', 1, 1);
SET @id_ejercicio_poemas = LAST_INSERT_ID();

-- Paso 5: Agregar pregunta (TEXTO CORREGIDO Y MÁS CORTO)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos) VALUES
(@id_ejercicio_poemas, '¿Qué palabras riman en el siguiente poema?\n"Estrellita dónde estás\nMe pregunto qué serás"', 'opcion_multiple', 1, 1.00);
SET @id_pregunta_poemas = LAST_INSERT_ID();

-- Paso 6: Agregar opciones
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES
(@id_pregunta_poemas, 'estás - serás', 1, 1),
(@id_pregunta_poemas, 'dónde - pregunto', 0, 2),
(@id_pregunta_poemas, 'estrellita - me', 0, 3);

-- Paso 7: Agregar recurso
INSERT INTO tbl_recurso_adjunto (id_subtema, id_tipo_recurso, orden, titulo, url, mime_type, tamano_bytes, descripcion) VALUES
(@id_subtema_poemas, 4, 1, 'Imagen poema estrellita', 'https://ejemplo.com/estrellita.png', 'image/png', 15000, 'Ilustración del poema Estrellita');

-- VERIFICACIÓN FINAL 🎉
SELECT 'Poemas agregado (Quitar indicador)' AS mensaje;
SELECT
    s.id_subtema,
    s.nombre AS subtema,
    t.nombre AS tema,
    m.nombre AS materia,
    e.nombre AS ejercicio
FROM tbl_subtema s
JOIN tbl_tema t ON s.id_tema = t.id_tema
JOIN tbl_materia m ON t.id_materia = m.id_materia
JOIN tbl_ejercicio e ON s.id_subtema = e.id_subtema
WHERE s.nombre = 'Poemas en español';

-- Mostrar la pregunta corregida
SELECT 'Pregunta corregida:' AS info, enunciado FROM tbl_pregunta
WHERE enunciado LIKE '%Estrellita dónde estás%';

select * from tbl_rol;
select * from tbl_usuario;