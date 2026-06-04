USE smartlearn;

-- ============================================================
-- 1. INSERTAR MATERIA
-- ============================================================
INSERT INTO tbl_materia (nombre, slug, descripcion)
VALUES (
           'Mi entorno',
           'https://res.cloudinary.com/dlchxvwae/image/upload/v1780532241/materia-miEntorno_rrmmpn.png',
           'Materia enfocada en el reconocimiento del medio social, natural y climático que rodea a los estudiantes.'
       );

-- Capturamos el ID de la materia recién creada
SET @id_materia_entorno = LAST_INSERT_ID();


-- ============================================================
-- 2. INSERTAR TEMAS
-- ============================================================
USE smartlearn;

-- NOTA: Si ejecutas este script en una sesión nueva donde no exista @id_materia_entorno,
-- puedes descomentar la siguiente línea para buscar su ID automáticamente:
-- SELECT id_materia INTO @id_materia_entorno FROM tbl_materia WHERE slug = 'mi-entorno';


-- ============================================================
-- 1. INSERTAR NUEVOS TEMAS (Tema 3 y Tema 4)
-- ============================================================

-- Tema 3: Mi rutina diaria
INSERT INTO tbl_tema (id_materia, nombre, orden)
VALUES (@id_materia_entorno, 'Mi rutina diaria', 3);
SET @id_tema_rutina = LAST_INSERT_ID();

-- Tema 4: La vida en la granja
INSERT INTO tbl_tema (id_materia, nombre, orden)
VALUES (@id_materia_entorno, 'La vida en la granja', 4);
SET @id_tema_vida_granja = LAST_INSERT_ID();


-- ============================================================
-- 2. INSERTAR SUBTEMAS (CORREGIDO)
-- ============================================================

-- Subtema para Tema 3
INSERT INTO tbl_subtema (id_tema, nombre, orden)
VALUES (@id_tema_rutina, 'Los días y mis actividades', 1);
SET @id_subtema_actividades = LAST_INSERT_ID();

-- Subtema para Tema 4
INSERT INTO tbl_subtema (id_tema, nombre, orden)
VALUES (@id_tema_vida_granja, 'Conociendo a los animales y sus amigos', 1);
SET @id_subtema_conocimiento_animales = LAST_INSERT_ID();


-- ============================================================
-- 3. INSERTAR TEXTOS TEÓRICOS (tbl_teoria)
-- ============================================================

-- Teoría: Los días y mis actividades
INSERT INTO tbl_teoria (id_subtema, contenido, revisado, fuente)
VALUES (
           @id_subtema_actividades,
           'Cada día es una oportunidad para aprender cosas nuevas. La semana tiene siete días: lunes, martes, miércoles, jueves, viernes, sábado y domingo.\n\nHoy es un gran día para jugar y hacer la tarea. Ayer fuimos a pasear en bicicleta y mañana nos toca ir a la escuela. En la mañana siempre usamos los zapatos y saludamos diciendo "hola" y "buenos días". En la noche, cuando nos sentimos un poco cansado, es momento de descansar en la cama y dormir muy feliz.',
           1,
           'SmartLearn Content Team'
       );

-- Teoría: Conociendo a los animales y sus amigos
INSERT INTO tbl_teoria (id_subtema, contenido, revisado, fuente)
VALUES (
           @id_subtema_conocimiento_animales,
           'Los animales de la granja son muy especiales y viven en un lugar muy bonito. A la vaca le gusta mucho comer pasto y nos da leche para hacer queso. El cerdo es gordo y muy chistoso, le encanta estar en su corral. La oveja tiene lana de color blanco que es muy suave, y el burro es muy fuerte y ayuda a cargar cosas en el campo. El gallo y la gallina caminan buscando comida y nos regalan huevos cada mañana.\n\nUn niño y su familia cuidan a los animales todos los días. Con mucho amor, les dan agua limpia y comida. Los animales se ponen muy feliz cuando las personas los ayudan. Si visitas una granja, siempre recuerda decir "por favor" y "gracias" a las personas que trabajan ahí cuidando la naturaleza.',
           1,
           'SmartLearn Content Team'
       );


USE smartlearn;

-- ============================================================
-- 6. INSERTAR EJERCICIOS (tbl_ejercicio)
-- ============================================================

-- Ejercicio Práctico para el Tema 3 (Mi rutina diaria)
INSERT INTO tbl_ejercicio (id_subtema, nombre, tipo, dificultad, orden, activo)
VALUES (
           @id_subtema_actividades,
           'Repaso: Los días de la semana',
           'practica',
           1,
           1,
           1
       );
SET @id_ejercicio_rutina = LAST_INSERT_ID();

-- Ejercicio de Evaluación para el Tema 4 (La vida en la granja)
INSERT INTO tbl_ejercicio (id_subtema, nombre, tipo, dificultad, orden, activo)
VALUES (
           @id_subtema_conocimiento_animales,
           'Quiz: ¿Quién es quién en la granja?',
           'evaluacion',
           2,
           1,
           1
       );
SET @id_ejercicio_granja = LAST_INSERT_ID();

-- ============================================================
-- 7. INSERTAR PREGUNTAS (tbl_pregunta)
-- ============================================================

-- ---- PREGUNTAS PARA EL EJERCICIO 1 (Rutina) ----
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (@id_ejercicio_rutina, '¿Cuántos días tiene una semana completa?', 'opcion_multiple', 1, 1.0);
SET @id_preg_rutina_1 = LAST_INSERT_ID();

INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (@id_ejercicio_rutina, '¿Qué palabras usamos en la mañana para saludar?', 'opcion_multiple', 2, 1.0);
SET @id_preg_rutina_2 = LAST_INSERT_ID();

-- ---- PREGUNTAS PARA EL EJERCICIO 2 (Granja) ----
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (@id_ejercicio_granja, '¿Qué animal nos da leche para hacer queso?', 'opcion_multiple', 1, 1.0);
SET @id_preg_granja_1 = LAST_INSERT_ID();

INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (@id_ejercicio_granja, 'El animal que tiene lana blanca y suave es...', 'opcion_multiple', 2, 1.0);
SET @id_preg_granja_2 = LAST_INSERT_ID();

-- ============================================================
-- 8. INSERTAR OPCIONES (tbl_opcion)
-- ============================================================
-- IMPORTANTE: Solo UNA opción debe tener es_correcta = 1 por pregunta
-- para respetar el trigger 'trg_opcion_unique_correcta_insert'.

-- Opciones para Pregunta 1 (Rutina)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_rutina_1, 'Cinco días', 0, 1);
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_rutina_1, 'Diez días', 0, 2);
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_rutina_1, 'Siete días', 1, 3); -- CORRECTA

-- Opciones para Pregunta 2 (Rutina)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_rutina_2, '"Hola" y "Buenos días"', 1, 1); -- CORRECTA
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_rutina_2, '"Adiós" y "Buenas noches"', 0, 2);
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_rutina_2, '"Hasta luego"', 0, 3);

-- Opciones para Pregunta 1 (Granja)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_granja_1, 'La gallina', 0, 1);
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_granja_1, 'La vaca', 1, 2); -- CORRECTA
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_granja_1, 'El cerdo', 0, 3);

-- Opciones para Pregunta 2 (Granja)
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_granja_2, 'El burro', 0, 1);
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_granja_2, 'La oveja', 1, 2); -- CORRECTA
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden) VALUES (@id_preg_granja_2, 'El gallo', 0, 3);

-- ============================================================
-- FIN DEL SCRIPT DE LLENADO DE MATERIA
-- ============================================================