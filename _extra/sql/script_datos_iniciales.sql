USE smartlearn;

-- ============================================================
-- 0. LIMPIAR DATOS PREVIOS
-- ============================================================
SET
FOREIGN_KEY_CHECKS = 0;
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
SET
FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. CATÁLOGOS BÁSICOS
-- ============================================================

INSERT INTO tbl_rol (tipo, descripcion)
VALUES ('ESTUDIANTE', 'Usuario que estudia las materias'),
       ('PROFESOR', 'Crea y gestiona contenido'),
       ('ADMIN', 'Gestión completa del sistema');

-- id_tipo_recurso: 1=PDF, 2=Video, 3=Enlace, 4=Imagen, 5=Audio
INSERT INTO tbl_tipo_recurso (nombre, descripcion)
VALUES ('PDF', 'Documento en formato PDF descargable'),
       ('Video', 'Video explicativo de YouTube/Vimeo'),
       ('Enlace', 'Enlace externo a recurso educativo'),
       ('Imagen', 'Diagrama, infografía o imagen explicativa'),
       ('Audio', 'Podcast o audio explicativo');

-- ============================================================
-- 2. MATERIAS
-- id_materia: 1=Matemáticas Básicas  2=Álgebra  3=Geometría
--             4=Español  5=Historia de México
-- ============================================================

INSERT INTO tbl_materia (nombre, slug, descripcion)
VALUES ('Matemáticas Básicas', 'matematicas-basicas', 'Conceptos fundamentales de matemáticas'),
       ('Álgebra', 'algebra', 'Operaciones algebraicas y ecuaciones'),
       ('Geometría', 'geometria', 'Figuras geométricas y teoremas'),
       ('Español', 'espanol', 'Gramática y literatura española'),
       ('Historia de México', 'historia-de-mexico', 'Curso básico de historia de México');

-- ============================================================
-- 3. TEMAS
-- id_tema:
--   1  Números Naturales          (mat 1)
--   2  Operaciones Básicas        (mat 1)
--   3  Fracciones                 (mat 1)
--   4  Expresiones Algebraicas    (mat 2)
--   5  Ecuaciones Lineales        (mat 2)
--   6  Figuras Planas             (mat 3)
--   7  Áreas y Perímetros         (mat 3)
--   8  Ortografía                 (mat 4)
--   9  Gramática                  (mat 4)
--  10  Literatura                 (mat 4)
--  11  Bloque 1: Conociendo el pasado         (mat 5)
--  12  Bloque 2: Las primeras personas        (mat 5)
--  13  Bloque 3: Grandes civilizaciones       (mat 5)
--  14  Bloque 4: La llegada de los españoles  (mat 5)
--  15  Bloque 5: México como colonia          (mat 5)
--  16  Bloque 6: La independencia             (mat 5)
--  17  Bloque 7: El México libre              (mat 5)
--  18  Bloque 8: Porfiriato y Revolución      (mat 5)
-- ============================================================

-- Matemáticas Básicas (mat 1)
INSERT INTO tbl_tema (id_materia, nombre, orden)
VALUES (1, 'Números Naturales', 1),
       (1, 'Operaciones Básicas', 2),
       (1, 'Fracciones', 3);

-- Álgebra (mat 2)
INSERT INTO tbl_tema (id_materia, nombre, orden)
VALUES (2, 'Expresiones Algebraicas', 1),
       (2, 'Ecuaciones Lineales', 2);

-- Geometría (mat 3)
INSERT INTO tbl_tema (id_materia, nombre, orden)
VALUES (3, 'Figuras Planas', 1),
       (3, 'Áreas y Perímetros', 2);

-- Español (mat 4)
INSERT INTO tbl_tema (id_materia, nombre, orden)
VALUES (4, 'Ortografía', 1),
       (4, 'Gramática', 2),
       (4, 'Literatura', 3);

-- Historia de México (mat 5)
INSERT INTO tbl_tema (id_materia, nombre)
VALUES (5, 'Bloque 1: Conociendo el pasado'),
       (5, 'Bloque 2: Las primeras personas en México'),
       (5, 'Bloque 3: Grandes civilizaciones antiguas'),
       (5, 'Bloque 4: La llegada de los españoles'),
       (5, 'Bloque 5: México como colonia'),
       (5, 'Bloque 6: La independencia de México'),
       (5, 'Bloque 7: El México libre y sus luchas'),
       (5, 'Bloque 8: Porfiriato y Revolución Mexicana');

-- ============================================================
-- 4. SUBTEMAS
-- id_subtema:
--   1  Clasificación de números   (tema 1)
--   2  Valor posicional           (tema 1)
--   3  Suma y resta               (tema 2)
--   4  Multiplicación             (tema 2)
--   5  División                   (tema 2)
--   6  Variables y constantes     (tema 4)
--   7  Simplificación             (tema 4)
--   8  Poemas en español          (tema 10)
--   9  Conociendo el pasado       (tema 11)
--  10  Las primeras personas      (tema 12)
--  11  Grandes civilizaciones     (tema 13)
--  12  La llegada de los españoles(tema 14)
--  13  México como colonia        (tema 15)
--  14  La independencia de México (tema 16)
--  15  El México libre y sus luchas(tema 17)
--  16  Porfiriato y Revolución    (tema 18)
-- ============================================================

-- Números Naturales (tema 1)
INSERT INTO tbl_subtema (id_tema, nombre, orden)
VALUES (1, 'Clasificación de números', 1),
       (1, 'Valor posicional', 2);

-- Operaciones Básicas (tema 2)
INSERT INTO tbl_subtema (id_tema, nombre, orden)
VALUES (2, 'Suma y resta', 1),
       (2, 'Multiplicación', 2),
       (2, 'División', 3);

-- Expresiones Algebraicas (tema 4)
INSERT INTO tbl_subtema (id_tema, nombre, orden)
VALUES (4, 'Variables y constantes', 1),
       (4, 'Simplificación', 2);

-- Literatura (tema 10)
INSERT INTO tbl_subtema (id_tema, nombre, orden)
VALUES (10, 'Poemas en español', 1);

-- Historia de México — un subtema por bloque (temas 11..18)
INSERT INTO tbl_subtema (id_tema, nombre)
VALUES (11, 'Conociendo el pasado'),
       (12, 'Las primeras personas en México'),
       (13, 'Grandes civilizaciones antiguas'),
       (14, 'La llegada de los españoles'),
       (15, 'México como colonia'),
       (16, 'La independencia de México'),
       (17, 'El México libre y sus luchas'),
       (18, 'Porfiriato y Revolución Mexicana');

-- ============================================================
-- 5. TEORÍA
-- id_teoria sigue el mismo orden de inserción (1..N)
-- ============================================================

INSERT INTO tbl_teoria (id_subtema, contenido, revisado, fuente)
VALUES
-- subtema 1: Clasificación de números
(1,
 'Los números naturales se clasifican en: pares, impares, primos y compuestos. Un número primo solo es divisible por 1 y por sí mismo.',
 1, 'Libro de texto 1º primaria'),
-- subtema 2: Valor posicional
(2, 'En el número 1234: 4=unidades, 3=decenas, 2=centenas, 1=millares.', 1, 'https://ejemplo.com/valor-posicional'),
-- subtema 3: Suma y resta
(3, 'bienvenido curso asiento', 1, 'Khan Academy'),
-- subtema 4: Multiplicación
(4, 'Multiplicación: 12 × 34 = 408. Método de multiplicación por filas.', 1, NULL),
-- subtema 6: Variables y constantes
(6, 'Variable: x, y (cambian). Constante: 5, 10 (fijas). Ejemplo: 2x + 3 = 7', 1, 'Álgebra básica'),
-- subtema 8: Poemas en español
(8,
 'Los poemas son textos literarios que usan ritmo, rima y figuras literarias. Ejemplo sencillo:\n\n"Estrellita dónde estás\nMe pregunto qué serás\nEn el cielo y en el mar\nUn diamante de verdad."\n\nEste poema tiene rima (estás/serás) y ritmo.',
 1, 'Poesía infantil básica');

-- Historia de México (subtemas 9..16)
INSERT INTO tbl_teoria (id_subtema, contenido, revisado)
VALUES (9,
        '¿Qué es la historia? La historia es el estudio de los acontecimientos pasados que han influido en la humanidad. Nos ayuda a comprender cómo vivían las personas en diferentes épocas, qué desafíos enfrentaron y cómo sus acciones han moldeado el mundo en el que vivimos hoy. A través de la historia, podemos aprender sobre las culturas, tradiciones y eventos que han dejado una huella en la sociedad. Para las personas con discapacidad visual, la historia se puede explorar mediante narraciones auditivas, relatos orales y descripciones detalladas que permiten imaginar los escenarios y comprender los contextos históricos. La tecnología también ofrece herramientas como audiolibros y aplicaciones accesibles que facilitan el aprendizaje de la historia de manera inclusiva. ¿Por qué es importante conocer lo que pasó antes? Conocer la historia nos permite entender el presente y prepararnos para el futuro. Al estudiar los errores y aciertos del pasado, podemos tomar decisiones más informadas y evitar repetir equivocaciones. Además, la historia nos enseña sobre la evolución de los derechos humanos, las luchas por la justicia y los avances sociales que han beneficiado a muchas personas. El tiempo histórico: pasado, presente y futuro. El tiempo histórico se refiere a la manera en que organizamos y comprendemos los eventos a lo largo del tiempo. Se divide en tres momentos: pasado, presente y futuro. Pasado: Incluye todos los eventos que ya han ocurrido. Por ejemplo, la independencia de México en 1821. Presente: Es el momento actual en el que vivimos. Por ejemplo, el día de hoy y las actividades que realizamos. Futuro: Se refiere a lo que aún no ha sucedido. Por ejemplo, los planes y metas que tenemos para los próximos años. Cómo se cuenta la historia: relatos, leyendas, y testimonios orales. La historia se transmite a través de diversas fuentes que nos proporcionan información sobre el pasado. Estas fuentes pueden ser: Orales: Relatos y testimonios contados por personas que vivieron ciertos eventos o que han transmitido historias de generación en generación. Escritas: Documentos, cartas, libros y registros que contienen información sobre hechos históricos. Materiales: Objetos, monumentos y artefactos que nos dan pistas sobre cómo vivían las personas en otras épocas.',
        0),
       (10,
        '¿Cómo eran las primeras comunidades? Las primeras comunidades humanas en lo que hoy es México se formaron hace miles de años. Estas personas vivían en pequeños grupos y se desplazaban constantemente en busca de alimento. No tenían casas permanentes; en su lugar, utilizaban refugios temporales como cuevas o estructuras hechas con ramas y hojas. La vida en estas comunidades era sencilla pero colaborativa. Todos los miembros del grupo tenían responsabilidades: algunos se encargaban de buscar comida, otros de cuidar a los niños o de preparar herramientas. La cooperación era esencial para sobrevivir en un entorno natural lleno de desafíos. Estas comunidades también compartían conocimientos a través de historias y enseñanzas orales. De esta manera, las habilidades y experiencias se transmitían de generación en generación, fortaleciendo los lazos entre sus miembros. Aunque no dejaron escritos, su legado perdura en las herramientas que fabricaron, los restos de sus campamentos y las tradiciones que, en algunos casos, han llegado hasta nuestros días. Cazadores y recolectores. Durante la mayor parte de la prehistoria, los seres humanos fueron cazadores y recolectores. Esto significa que obtenían su alimento cazando animales y recolectando frutas, semillas y raíces. Esta forma de vida requería un profundo conocimiento del entorno natural y una gran habilidad para rastrear y cazar. En el territorio que hoy es México, se han encontrado evidencias de estas actividades. Por ejemplo, en Tultepec, Estado de México, arqueólogos descubrieron trampas para mamuts construidas hace aproximadamente 15,000 años. Estas estructuras muestran la inteligencia y cooperación de estos grupos para cazar animales de gran tamaño. Además de cazar, recolectaban una variedad de plantas comestibles. Conocían las temporadas en las que ciertas frutas estaban maduras y sabían cuáles eran seguras para el consumo. Este conocimiento era vital para su supervivencia y se transmitía oralmente entre generaciones. La vida de los cazadores y recolectores estaba en constante movimiento. Seguían a los animales y se desplazaban según las estaciones del año. Esta movilidad les permitía adaptarse a diferentes entornos y aprovechar los recursos disponibles en cada región. Nacimiento de la agricultura. Con el tiempo, algunas comunidades comenzaron a observar que podían plantar semillas y cultivar sus propios alimentos. Este descubrimiento marcó el inicio de la agricultura. Al aprender a sembrar y cosechar, las personas ya no dependían únicamente de la caza y la recolección. La agricultura permitió que las comunidades se establecieran en un lugar fijo, dando origen a los primeros asentamientos permanentes. Cultivaban maíz, frijol, calabaza y otros alimentos básicos que siguen siendo fundamentales en la dieta mexicana. Además de cultivar plantas, comenzaron a domesticar animales como perros, pavos y abejas. Esto les proporcionaba carne, huevos, miel y compañía. La combinación de agricultura y domesticación de animales mejoró significativamente su calidad de vida. Periodos Prehispánicos en México. Etapa Lítica (Paleoindio) Aproximadamente 30,000 a.C. a 2,500 a.C: La Etapa Lítica marca los primeros momentos de ocupación humana en lo que hoy conocemos como México. Durante este periodo, los grupos humanos eran nómadas y se dedicaban a la caza, la recolección y la pesca para subsistir. Fabricaban herramientas de piedra, como puntas de proyectil, que les ayudaban en sus actividades cotidianas. Estos grupos no tenían viviendas permanentes; se desplazaban según las estaciones del año y la disponibilidad de recursos. La transmisión de conocimientos se realizaba de manera oral, compartiendo experiencias y enseñanzas que permitían la adaptación al entorno. Preclásico - Aproximadamente 2,500 a.C. a 200 d.C. El Preclásico es un periodo de transición donde las comunidades comenzaron a establecerse de manera más permanente. La agricultura se consolidó como la principal actividad económica, cultivando plantas como el maíz, el frijol, la calabaza y el chile. Esto permitió el crecimiento de aldeas y el desarrollo de estructuras sociales más complejas. Durante este tiempo, surgieron las primeras grandes civilizaciones en Mesoamérica, como los olmecas, quienes son considerados la "cultura madre" debido a su influencia en otras culturas posteriores. Desarrollaron sistemas de escritura, calendarios y una rica tradición artística y religiosa. La vida cotidiana en el Preclásico estaba marcada por la organización comunitaria, la construcción de centros ceremoniales y el intercambio de bienes y conocimientos con otras regiones. Clásico - Aproximadamente 200 d.C. a 800 d.C. El periodo Clásico se caracteriza por el auge de grandes ciudades y civilizaciones. En el sur de Mesoamérica, floreció la civilización maya, destacando por sus avances en astronomía, matemáticas y escritura jeroglífica. Construyeron impresionantes ciudades como Tikal, Palenque y Copán, con templos, pirámides y observatorios astronómicos. En el centro de México, la ciudad de Teotihuacán alcanzó su máximo esplendor. Su urbanismo planificado, con avenidas amplias y monumentos como la Pirámide del Sol y la Pirámide de la Luna, reflejaban una sociedad organizada y avanzada. Durante este periodo, las redes de comercio y comunicación se expandieron, facilitando el intercambio cultural y tecnológico entre diferentes regiones de Mesoamérica. Posclásico - Aproximadamente 800 d.C. a 1521 d.C. El Posclásico es un periodo de grandes transformaciones y conflictos. Civilizaciones como los toltecas, los zapotecas, los mixtecos y los mexicas (aztecas) dominaron diversas regiones de Mesoamérica. Los mexicas, por ejemplo, establecieron el imperio más grande de la época, con su capital en Tenochtitlán, ubicada en lo que hoy es la Ciudad de México. Este periodo estuvo marcado por una intensa actividad militar, expansión territorial y la consolidación de estructuras políticas y religiosas complejas. Las ciudades-estado competían por el control de recursos y territorios, lo que llevó a alianzas y conflictos bélicos. A pesar de los desafíos, las civilizaciones del Posclásico dejaron un legado perdurable en la arquitectura, el arte, la religión y las tradiciones que aún influyen en la cultura mexicana contemporánea. Las culturas madre: olmecas. Los olmecas son conocidos como la "cultura madre" de Mesoamérica porque influyeron en muchas civilizaciones posteriores. Vivieron en las regiones costeras del Golfo de México, principalmente en los actuales estados de Veracruz y Tabasco, alrededor del año 1200 a.C. Una de las características más impresionantes de los olmecas son sus enormes cabezas de piedra talladas, que pueden medir hasta tres metros de altura. Estas esculturas muestran rostros humanos con rasgos distintivos y se cree que representan a líderes importantes de su sociedad. Los olmecas desarrollaron un sistema de escritura y un calendario, y fueron pioneros en la construcción de centros ceremoniales con pirámides y plazas. Su arte y arquitectura reflejan una profunda conexión con la naturaleza y lo espiritual. La influencia olmeca se extendió a través del comercio y el intercambio cultural. Muchas de sus ideas, estilos artísticos y prácticas religiosas fueron adoptadas por civilizaciones posteriores como los mayas y los mexicas, dejando un legado duradero en la historia de México.',
        0),
       (11,
        'Teotihuacanos: La ciudad de los dioses. Teotihuacán, cuyo nombre significa "lugar donde fueron creados los dioses", fue una de las ciudades más importantes de Mesoamérica. Ubicada en el Valle de México, alcanzó su apogeo entre los siglos I y VII d.C. Su influencia se extendió por gran parte de la región, desde el norte hasta el sur del actual México, así como Guatemala y Honduras. La ciudad era conocida por su impresionante arquitectura, destacando la Pirámide del Sol y la Pirámide de la Luna. Estas estructuras eran centros ceremoniales donde se realizaban rituales religiosos. Además, Teotihuacán contaba con una red de calzadas que conectaban diferentes áreas de la ciudad, facilitando el comercio y la comunicación. Aunque la escritura teotihuacana no ha sido completamente descifrada, se han encontrado murales y esculturas que representan a sus habitantes y sus creencias. Estos artefactos ofrecen una visión de la vida cotidiana y las prácticas religiosas de esta civilización. La caída de Teotihuacán, alrededor del siglo VII, sigue siendo un misterio. Sin embargo, su legado perdura en las influencias culturales y arquitectónicas que dejó en otras civilizaciones mesoamericanas. Mayas: Sabiduría y astronomía. La civilización maya floreció en el sureste de México, Guatemala, Belice y partes de Honduras y El Salvador. Se destacó por sus avances en astronomía, matemáticas y escritura. Desarrollaron un sistema de numeración vigesimal (basado en el número 20) y fueron pioneros en el uso del concepto del cero. Los mayas construyeron impresionantes ciudades como Tikal, Palenque y Chichén Itzá. Estas ciudades contaban con pirámides escalonadas, templos, observatorios astronómicos y plazas ceremoniales. La arquitectura maya reflejaba su profundo conocimiento del cosmos y su conexión con los dioses. La escritura maya, conocida como jeroglífica, era utilizada para registrar eventos históricos, genealogías y rituales religiosos. Aunque muchos de sus códices fueron destruidos durante la colonización, algunos han sobrevivido y continúan siendo estudiados por arqueólogos y epigrafistas. A pesar de su colapso en el periodo Clásico Tardío (alrededor del siglo IX), la cultura maya persiste en sus descendientes actuales, quienes mantienen vivas muchas de sus tradiciones y lenguas. Mexicas: El imperio del sol. Los mexicas, también conocidos como aztecas, fueron una civilización que se asentó en el Valle de México en el siglo XIV. Fundaron la ciudad de Tenochtitlan, que se convirtió en la capital de su imperio. Este imperio abarcaba gran parte del centro y sur de México y estaba compuesto por una red de ciudades-estado aliadas. La sociedad mexica estaba organizada jerárquicamente. En la cima se encontraba el tlatoani, el gobernante supremo; seguido por los nobles, sacerdotes, guerreros y comerciantes; y en la base, los macehualtin, campesinos y artesanos. La religión desempeñaba un papel central en la vida mexica, con numerosos dioses asociados a aspectos de la naturaleza y la vida cotidiana. Los mexicas eran conocidos por sus habilidades en la guerra, el comercio y la agricultura. Cultivaban maíz, frijol, chile y cacao, y desarrollaron chinampas, islas artificiales construidas en lagos, para ampliar su superficie cultivable. La llegada de los españoles en el siglo XVI marcó el fin del imperio mexica. Sin embargo, su legado perdura en la Ciudad de México, que se construyó sobre las ruinas de Tenochtitlan, y en las tradiciones, lengua y cultura de sus descendientes.',
        0),
       (12,
        'Primeros contactos con los pueblos indígenas. En 1517, los primeros exploradores españoles llegaron a la península de Yucatán, donde establecieron contacto con los pueblos mayas. Estos encuentros iniciales fueron relativamente pacíficos, y los mayas recibieron a los extranjeros con hospitalidad. Sin embargo, las posteriores expediciones españolas tuvieron un carácter más agresivo, lo que alteró las relaciones con los pueblos indígenas. En 1519, Hernán Cortés, desobedeciendo órdenes del gobernador de Cuba, Diego Velázquez, emprendió una expedición hacia el territorio del actual México. A su llegada, estableció alianzas con pueblos indígenas como los tlaxcaltecas, quienes compartían intereses comunes con los españoles debido a su rivalidad con el Imperio Mexica. La llegada a Tenochtitlán. El 8 de noviembre de 1519, Cortés y su ejército llegaron a Tenochtitlán, la capital del Imperio Mexica, donde fueron recibidos por el emperador Moctezuma II. Moctezuma, posiblemente influenciado por presagios y leyendas sobre el regreso de Quetzalcoatl, recibió a los españoles con obsequios y hospitalidad. Sin embargo, este encuentro fue una mezcla de fascinación y desconfianza, ya que los mexicas no comprendían completamente las intenciones de los visitantes. La caída de Tenochtitlán. La relación entre los españoles y los mexicas se deterioró rápidamente. En 1520, tras la muerte de Moctezuma y una serie de enfrentamientos, los españoles fueron expulsados de Tenochtitlán en un evento conocido como la "Noche Triste". Sin embargo, Cortés regresó al año siguiente con refuerzos y aliados indígenas, sitiando la ciudad durante varios meses. Finalmente, el 13 de agosto de 1521, Tenochtitlán cayó, marcando el fin del Imperio Mexica y el inicio de la colonización española en la región. A pesar de estos desafíos, muchos pueblos indígenas resistieron y preservaron aspectos fundamentales de su identidad cultural. Hoy en día, las lenguas, tradiciones y cosmovisiones indígenas siguen siendo una parte vital del patrimonio cultural de México.',
        0),
       (13,
        'La organización del Virreinato de la Nueva España. Tras la caída de Tenochtitlán, los españoles establecieron el Virreinato de la Nueva España, que abarcaba gran parte de América del Norte, Central y el Caribe. La capital fue Ciudad de México, construida sobre las ruinas de Tenochtitlán. El virrey era el representante del rey de España y tenía autoridad sobre la administración, la economía y la religión en el territorio. La sociedad colonial estaba jerárquicamente organizada. En la cima se encontraban los peninsulares, nacidos en España; seguidos por los criollos, hijos de españoles nacidos en América; mestizos, producto de la mezcla entre europeos e indígenas; indígenas, que eran la mayoría de la población; y afrodescendientes, traídos como esclavos. La evangelización y el mestizaje. Uno de los principales objetivos de los colonizadores fue la evangelización de los pueblos indígenas. Los misioneros, principalmente franciscanos, dominicos y agustinos, establecieron conventos y escuelas para enseñar el cristianismo, el idioma español y las costumbres europeas. Este proceso llevó a la conversión forzada de muchos indígenas y a la destrucción de sus creencias y prácticas religiosas. El mestizaje, resultado de la mezcla entre europeos, indígenas y africanos, dio lugar a una nueva identidad cultural. Este fenómeno se reflejó en el arte, la comida, la música y las costumbres, creando una rica diversidad cultural que caracteriza a México hasta la fecha. La economía colonial. La economía del Virreinato de la Nueva España se basaba en la explotación de los recursos naturales y la mano de obra indígena. Las encomiendas eran sistemas en los que los colonizadores recibían tierras y el derecho a cobrar tributos a los indígenas, a cambio de su protección y evangelización. Sin embargo, en la práctica, muchos indígenas fueron sometidos a trabajos forzados en minas y plantaciones. El oro y la plata extraídos de las minas de Zacatecas y Guanajuato fueron fundamentales para la economía colonial. Además, se cultivaban productos como el azúcar, el cacao y el maíz, que eran exportados a Europa. Este modelo económico favoreció a los colonizadores y enriqueció a la Corona española, mientras que los pueblos indígenas sufrían explotación y marginación.',
        0),
       (14,
        'Antecedentes de la lucha por la independencia. La lucha por la independencia de México fue el resultado de diversos factores internos y externos que generaron un descontento generalizado entre los habitantes del Virreinato de la Nueva España. Entre los factores internos se encuentran las desigualdades sociales y económicas, la explotación de los pueblos indígenas y la creciente influencia de las ideas ilustradas que promovían la libertad y la igualdad. Por otro lado, los factores externos incluyen las revoluciones en otras partes del mundo, como la Independencia de las Trece Colonias en América del Norte (1776) y la Revolución Francesa (1789), que inspiraron a los mexicanos a luchar por su autonomía. El inicio del movimiento insurgente. El movimiento de independencia comenzó el 16 de septiembre de 1810, cuando el sacerdote Miguel Hidalgo y Costilla emitió el famoso "Grito de Dolores" en el pueblo de Dolores, Guanajuato. Este llamado a la lucha reunió a miles de personas que se unieron para enfrentar al ejército colonial español. A lo largo de los años siguientes, otros líderes insurgentes como Ignacio Allende, Juan Aldama y José María Morelos continuaron la lucha, enfrentándose a las fuerzas realistas y buscando apoyo tanto dentro como fuera del país. La consolidación de la independencia. A pesar de las derrotas sufridas por los insurgentes en los primeros años del movimiento, la lucha continuó con renovado ímpetu. José María Morelos, por ejemplo, convocó el Congreso de Chilpancingo en 1813, donde se proclamó la independencia de México y se establecieron las bases para una nación libre y soberana. Sin embargo, la lucha no terminó con la muerte de Morelos en 1815. Fue hasta 1820, con la firma de los Tratados de Córdoba entre los insurgentes y las autoridades coloniales, que se reconoció la independencia de México. La consumación de la independencia. El 27 de septiembre de 1821, el Ejército Trigarante, formado por las fuerzas insurgentes y los realistas, entró triunfante en la Ciudad de México, marcando la consumación de la independencia. Este evento puso fin a más de 300 años de dominio colonial español y dio inicio a una nueva etapa en la historia de México como nación independiente.',
        0),
       (15,
        'El Imperio de Iturbide. En 1822, Iturbide fue proclamado emperador de México, estableciendo el Primer Imperio Mexicano. Sin embargo, su gobierno enfrentó múltiples desafíos, como la falta de reconocimiento internacional y la oposición interna de diversos grupos políticos. La falta de recursos económicos y las tensiones con el Congreso llevaron a la abdicación de Iturbide en 1823, dando paso a la proclamación de la República Federal. La República Federal y los conflictos internos. Con la adopción de la Constitución de 1824, México se organizó como una república federal, otorgando autonomía a los estados y estableciendo un sistema de gobierno basado en la división de poderes. Sin embargo, las diferencias ideológicas entre federalistas y centralistas generaron constantes enfrentamientos. Durante este periodo, el país experimentó numerosos cambios de gobierno, golpes de Estado y conflictos armados. Figuras como Antonio López de Santa Anna jugaron un papel destacado, alternando entre el poder y el exilio en múltiples ocasiones. La inestabilidad política dificultó el desarrollo económico y social de la nación. Pérdidas territoriales y conflictos internacionales. En 1836, Texas, entonces parte de México, declaró su independencia, lo que desencadenó una serie de conflictos con Estados Unidos. Posteriormente, en 1846, estalló la guerra entre México y Estados Unidos, culminando en 1848 con la firma del Tratado de Guadalupe Hidalgo. Este tratado significó la pérdida de más de la mitad del territorio mexicano, incluyendo California, Nuevo México y Arizona. Además, México enfrentó la llamada "Guerra de los Pasteles" en 1838, una intervención francesa motivada por reclamaciones económicas. Estos conflictos internacionales debilitaron aún más al país y evidenciaron la necesidad de reformas estructurales. La Reforma y la Constitución de 1857. En un esfuerzo por estabilizar el país y establecer un marco legal sólido, se promulgó la Constitución de 1857. Este documento consagró principios liberales como la libertad de expresión, la abolición de los privilegios eclesiásticos y militares, y la garantía de derechos individuales. Sin embargo, la implementación de estas reformas generó una fuerte oposición por parte de sectores conservadores y de la Iglesia, lo que desembocó en la Guerra de Reforma entre 1858 y 1861. A pesar de los desafíos, la Constitución de 1857 sentó las bases para la consolidación del Estado mexicano y la modernización de sus instituciones. La Intervención Francesa y el Segundo Imperio. Tras la Guerra de Reforma, México enfrentaba una grave crisis económica y suspendió el pago de su deuda externa. Esto llevó a que Francia, junto con España y el Reino Unido, interviniera en el país en 1861. Aunque España y el Reino Unido se retiraron, Francia continuó su intervención y, en 1864, estableció el Segundo Imperio Mexicano bajo el emperador Maximiliano de Habsburgo. Este régimen fue apoyado por los conservadores, pero enfrentó la resistencia de los liberales liderados por Benito Juárez. Con el retiro del apoyo francés y la captura de Maximiliano en 1867, el Segundo Imperio llegó a su fin, y la República fue restaurada. La República Restaurada. Con la restauración de la República en 1867, Benito Juárez retomó la presidencia y se enfocó en reconstruir el país, implementando reformas para fortalecer las instituciones y promover el desarrollo económico. A su muerte en 1872, Sebastián Lerdo de Tejada asumió la presidencia, continuando con las políticas liberales. Sin embargo, la reelección de Lerdo en 1876 fue impugnada por Porfirio Díaz, quien, mediante el Plan de Tuxtepec, se levantó en armas y asumió el poder, marcando el inicio de una nueva etapa en la historia de México.',
        0),
       (16,
        'El Porfiriato. El Porfiriato fue el periodo en el que Porfirio Díaz gobernó México, desde 1876 hasta 1911, con una breve interrupción entre 1880 y 1884. Durante estos años, el país experimentó un notable crecimiento económico y estabilidad política. Se construyeron más de 19,000 kilómetros de vías férreas, se modernizó la infraestructura y se promovió la inversión extranjera en sectores como la minería, la agricultura y la industria. Sin embargo, este progreso tuvo un alto costo social. La riqueza se concentró en manos de unos pocos, mientras que la mayoría de la población vivía en condiciones de pobreza y marginación. La represión política, la censura y la falta de libertades civiles caracterizaron el régimen de Díaz, quien se mantuvo en el poder mediante elecciones fraudulentas y el uso de la fuerza. Causas y estallido de la Revolución Mexicana. El descontento social acumulado durante el Porfiriato llevó al surgimiento de movimientos opositores. En 1910, Francisco I. Madero, un político y empresario del norte del país, lanzó el Plan de San Luis, en el que llamaba a la población a levantarse en armas el 20 de noviembre de ese año para derrocar a Díaz. La Revolución Mexicana comenzó como una insurrección contra la dictadura de Porfirio Díaz y continuó como una lucha entre diversas facciones revolucionarias. Las principales causas del conflicto fueron la crisis del Porfiriato, la desigualdad social en el campo y las campañas opositoras de Francisco Madero. Etapas y líderes de la Revolución. Tras la renuncia de Díaz en 1911, Madero asumió la presidencia, pero su gobierno enfrentó la oposición de diversos grupos. En 1913, un golpe de Estado conocido como la Decena Trágica resultó en el asesinato de Madero y el ascenso al poder de Victoriano Huerta. La oposición a Huerta unió a líderes revolucionarios como Venustiano Carranza, Francisco Villa y Emiliano Zapata. En 1914, Villa y Zapata se encontraron en la Ciudad de México, uniendo sus fuerzas en una histórica entrada a la capital. Sin embargo, las diferencias entre las facciones revolucionarias llevaron a nuevos conflictos internos. La Constitución de 1917 y el fin del conflicto armado. En 1917, bajo el liderazgo de Carranza, se promulgó una nueva Constitución que incorporó demandas sociales y políticas surgidas de la Revolución. La Constitución de 1917 fue la primera en el mundo por su contenido político y social, estableciendo derechos laborales, la propiedad de la tierra y la educación laica y gratuita. Aunque la promulgación de la Constitución marcó un hito importante, la violencia continuó en los años siguientes. Líderes como Zapata y Villa fueron asesinados en 1919 y 1923, respectivamente. No existe un consenso sobre cuándo terminó el proceso revolucionario; algunas fuentes lo sitúan en 1920 con la presidencia de Adolfo de la Huerta, mientras que otras lo extienden hasta los años 1940.',
        0);

-- ============================================================
-- 6. EJERCICIOS
-- id_ejercicio:
--   1  Ejercicio suma básica       (subtema 3)
--   2  Ejercicio de resta          (subtema 3)
--   3  Evaluación suma/resta       (subtema 3)
--   4  Tablas de multiplicar       (subtema 4)
--   5  Multiplicación 2 dígitos    (subtema 4)
--   6  Identifica la rima          (subtema 8)
--   7  Bloque 1 Historia           (subtema 9)
--   8  Bloque 2 Historia           (subtema 10)
--   9  Bloque 3 Historia           (subtema 11)
--  10  Bloque 4 Historia           (subtema 12)
--  11  Bloque 5 Historia           (subtema 13)
--  12  Bloque 6 Historia           (subtema 14)
--  13  Bloque 7 Historia           (subtema 15)
--  14  Bloque 8 Historia           (subtema 16)
--  15  Bloque 1 Mat básicas        (subtema 17)
--  16  Bloque 2 Mat básicas        (subtema 18)
--  17  Bloque 3 Mat básicas        (subtema 19)
--  18  Bloque 4 Mat básicas        (subtema 20)
--  19  Bloque 5 Mat básicas        (subtema 21)
--  20  Bloque 6 Mat básicas        (subtema 22)
--  21  Bloque 7 Mat básicas        (subtema 23)
--  22  Bloque 8 Mat básicas        (subtema 24)
-- ============================================================

-- Script 1 + Poemas
INSERT INTO tbl_ejercicio (id_subtema, nombre, tipo, dificultad, orden)
VALUES (3, 'Ejercicio de suma básica', 'practica', 1, 1),
       (3, 'Ejercicio de resta', 'practica', 2, 2),
       (3, 'Evaluación suma/resta', 'evaluacion', 3, 3),
       (4, 'Tablas de multiplicar', 'practica', 2, 1),
       (4, 'Multiplicación de 2 dígitos', 'evaluacion', 4, 2),
       (8, 'Identifica la rima en el poema', 'practica', 1, 1);

-- Historia de México (subtemas 9..16)
INSERT INTO tbl_ejercicio (id_subtema, nombre, tipo, dificultad, orden)
VALUES (9, 'Ejercicio Bloque 1: Conociendo el pasado', 'practica', 1, 1),
       (10, 'Ejercicio Bloque 2: Las primeras personas en México', 'practica', 1, 1),
       (11, 'Ejercicio Bloque 3: Grandes civilizaciones antiguas', 'practica', 1, 1),
       (12, 'Ejercicio Bloque 4: La llegada de los españoles', 'practica', 1, 1),
       (13, 'Ejercicio Bloque 5: México como colonia', 'practica', 1, 1),
       (14, 'Ejercicio Bloque 6: La independencia de México', 'practica', 1, 1),
       (15, 'Ejercicio Bloque 7: El México libre y sus luchas', 'practica', 1, 1),
       (16, 'Ejercicio Bloque 8: Porfiriato y Revolución Mexicana', 'practica', 1, 1);

-- ============================================================
-- 7. PREGUNTAS
-- Los id_ejercicio ahora correctos; los IDs de pregunta se asignan en orden.
--
-- Ejers 1-6  (script 1 + poemas)   → preguntas  1..4
-- Ejers 7-14 (Historia)            → preguntas  5..68
-- ============================================================

-- Ejercicio 1: suma básica
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (1, '¿Cuánto es 15 + 27?', 'opcion_multiple', 1, 1.00),
       (1, 'Calcula: 48 - 23', 'opcion_multiple', 2, 1.00);

-- Ejercicio 4: tablas de multiplicar
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (4, '¿Cuánto es 7 × 8?', 'opcion_multiple', 1, 1.00);

-- Ejercicio 6: poemas
INSERT INTO tbl_pregunta (id_ejercicio, enunciado, tipo, orden, puntos)
VALUES (6, '¿Qué palabras riman en el siguiente poema?\n"Estrellita dónde estás\nMe pregunto qué serás"',
        'opcion_multiple', 1, 1.00);

-- Historia — Ejercicio 7 (Bloque 1)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (7, '¿Qué estudia la historia?'),
       (7, '¿Por qué es importante conocer la historia?'),
       (7, '¿Cuál de los siguientes representa el tiempo histórico?'),
       (7, '¿Qué son los testimonios orales?'),
       (7, '¿Qué tipo de narración es una leyenda?'),
       (7, '¿Qué elemento ayuda a entender cómo vivían las personas en el pasado?'),
       (7, '¿Qué diferencia hay entre un relato histórico y una leyenda?'),
       (7, '¿Qué afirmación sobre el pasado, el presente y el futuro es correcta?');

-- Historia — Ejercicio 8 (Bloque 2)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (8, '¿Cómo vivían las primeras comunidades humanas en México?'),
       (8, '¿Qué actividades realizaban los cazadores y recolectores?'),
       (8, '¿Qué evento permitió que las comunidades se establecieran en lugares fijos?'),
       (8, '¿Qué alimento básico comenzaron a cultivar las primeras comunidades agrícolas?'),
       (8, '¿Qué periodo marca la presencia de los primeros grupos humanos en México?'),
       (8, '¿Qué civilización es considerada la "cultura madre" de Mesoamérica?'),
       (8, '¿Qué construyeron los olmecas que es reconocido por su tamaño e importancia cultural?'),
       (8, '¿Qué caracterizó al periodo Clásico en Mesoamérica?');

-- Historia — Ejercicio 9 (Bloque 3)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (9, '¿En qué Valle se ubicaba Teotihuacán?'),
       (9, '¿Cuál de las siguientes estructuras NO pertenece a Teotihuacán?'),
       (9, '¿Qué civilización mesoamericana destacó por sus conocimientos astronómicos y matemáticos?'),
       (9, '¿Qué base numérica utilizaban los mayas en su sistema de numeración?'),
       (9, '¿Qué civilización construyó la ciudad de Tenochtitlan?'),
       (9, '¿Qué tipo de estructura construyeron los mexicas para ampliar sus áreas de cultivo?'),
       (9, '¿Qué término describe a los campesinos y artesanos mexicas?'),
       (9, '¿Sobre qué antigua ciudad se construyó la actual Ciudad de México?');

-- Historia — Ejercicio 10 (Bloque 4)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (10, '¿En qué año emprendió Hernán Cortés su expedición hacia el actual México?'),
       (10, '¿Qué gobernador ordenó a Cortés no salir de Cuba?'),
       (10, '¿Qué pueblo indígena se alió con los españoles por rivalidad con los mexicas?'),
       (10, '¿Cómo se llamó el emperador mexica que recibió a Cortés en Tenochtitlán?'),
       (10, '¿Qué evento ocurrió en 1520 y marcó un retroceso temporal para los españoles?'),
       (10, '¿Qué creencia mexica pudo influir en la recepción de los españoles como seres divinos?'),
       (10, '¿Qué acontecimiento marca el fin del Imperio Mexica?'),
       (10, '¿Qué característica cultural indígena sigue viva en la actualidad?');

-- Historia — Ejercicio 11 (Bloque 5)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (11, '¿Cuál era la capital del Virreinato de la Nueva España?'),
       (11, '¿Quién estaba en la cima de la sociedad colonial?'),
       (11, '¿Qué grupo religioso impulsó la evangelización en la colonia?'),
       (11, '¿Qué actividad económica fue más importante en las minas coloniales?'),
       (11, '¿Qué eran las encomiendas?'),
       (11, '¿Qué productos agrícolas se exportaban desde la colonia?'),
       (11, '¿Qué impacto tuvo el mestizaje en la cultura?'),
       (11, '¿Quiénes eran los criollos?');

-- Historia — Ejercicio 12 (Bloque 6)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (12, '¿Cuál fue uno de los factores externos que influyó en la independencia de México?'),
       (12, '¿Qué personaje inició el movimiento de independencia en 1810?'),
       (12, '¿Cómo se llamó el evento donde Hidalgo convocó al pueblo a levantarse contra el dominio español?'),
       (12, '¿Quién continuó la lucha por la independencia tras la muerte de Hidalgo?'),
       (12, '¿Qué proclamó el Congreso de Chilpancingo en 1813?'),
       (12, '¿Qué documento firmaron en 1821 para reconocer la independencia?'),
       (12, '¿Qué ejército entró triunfante a la Ciudad de México el 27 de septiembre de 1821?');

-- Historia — Ejercicio 13 (Bloque 7)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (13, '¿Quién fue proclamado emperador de México en 1822?'),
       (13, '¿Qué estableció la Constitución de 1824?'),
       (13, '¿Qué personaje fue protagonista de varios gobiernos durante la república temprana?'),
       (13, '¿Cuál fue una consecuencia del Tratado de Guadalupe Hidalgo?'),
       (13, '¿Qué conflicto con Francia ocurrió en 1838?'),
       (13, '¿Qué principios estableció la Constitución de 1857?'),
       (13, '¿Qué emperador fue impuesto por Francia durante la Segunda Intervención?'),
       (13, '¿Qué plan permitió a Porfirio Díaz asumir el poder en 1876?');

-- Historia — Ejercicio 14 (Bloque 8)
INSERT INTO tbl_pregunta (id_ejercicio, enunciado)
VALUES (14, '¿Cuántos años gobernó Porfirio Díaz, aproximadamente, durante el Porfiriato?'),
       (14, '¿Cuál fue uno de los principales logros económicos del Porfiriato?'),
       (14, '¿Qué característica política marcó al régimen de Porfirio Díaz?'),
       (14, '¿Qué documento proclamó Francisco I. Madero en 1910 para llamar a la Revolución?'),
       (14, '¿Qué hecho marcó el inicio formal de la Revolución Mexicana?'),
       (14, '¿Qué suceso ocurrió durante la Decena Trágica en 1913?'),
       (14, '¿Qué líderes revolucionarios se encontraron en la Ciudad de México en 1914?'),
       (14, '¿Qué estableció la Constitución de 1917?');

-- ============================================================
-- 8. OPCIONES
-- El id_pregunta se asigna en orden de inserción.
-- Preguntas 1-4:   script 1 + poemas
-- Preguntas 5-12:  Historia Bloque 1
-- Preguntas 13-20: Historia Bloque 2
-- Preguntas 21-28: Historia Bloque 3
-- Preguntas 29-36: Historia Bloque 4
-- Preguntas 37-44: Historia Bloque 5
-- Preguntas 45-51: Historia Bloque 6
-- Preguntas 52-59: Historia Bloque 7
-- Preguntas 60-67: Historia Bloque 8
-- ============================================================

-- Pregunta 1: 15+27
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden)
VALUES (1, '42', 1, 1),
       (1, '32', 0, 2),
       (1, '52', 0, 3);

-- Pregunta 2: 48-23
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden)
VALUES (2, '25', 1, 1),
       (2, '71', 0, 2),
       (2, '35', 0, 3);

-- Pregunta 3: 7×8
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden)
VALUES (3, '56', 1, 1),
       (3, '48', 0, 2),
       (3, '65', 0, 3);

-- Pregunta 4: Poema rima
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta, orden)
VALUES (4, 'estás - serás', 1, 1),
       (4, 'dónde - pregunto', 0, 2),
       (4, 'estrellita - me', 0, 3);

-- ---- HISTORIA BLOQUE 1 (preguntas 5..12) ----

-- Pregunta 5: ¿Qué estudia la historia?
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (5, 'El futuro de la humanidad', 0),
       (5, 'Los eventos del pasado humano', 1),
       (5, 'Los sueños de las personas', 0),
       (5, 'Las predicciones astronómicas', 0);

-- Pregunta 6: ¿Por qué es importante?
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (6, 'Para repetir los mismos errores', 0),
       (6, 'Para comprender nuestro presente y construir un mejor futuro', 1),
       (6, 'Para olvidar el pasado', 0),
       (6, 'Para vivir en el pasado', 0);

-- Pregunta 7: Tiempo histórico
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (7, 'Solo el presente', 0),
       (7, 'El pasado, el presente y el futuro', 1),
       (7, 'Solo el pasado', 0),
       (7, 'Solo el futuro', 0);

-- Pregunta 8: Testimonios orales
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (8, 'Libros antiguos', 0),
       (8, 'Restos arqueológicos', 0),
       (8, 'Relatos que las personas transmiten de generación en generación', 1),
       (8, 'Documentos escritos por reyes', 0);

-- Pregunta 9: Leyenda
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (9, 'Una historia inventada sin relación con la cultura', 0),
       (9, 'Un relato basado en hechos reales pero con elementos fantásticos', 1),
       (9, 'Un informe científico', 0),
       (9, 'Un discurso político', 0);

-- Pregunta 10: Elemento histórico
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (10, 'Canciones modernas', 0),
       (10, 'Relatos históricos', 1),
       (10, 'Publicidad de televisión', 0),
       (10, 'Redes sociales', 0);

-- Pregunta 11: Relato vs leyenda
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (11, 'El relato histórico se basa en pruebas, la leyenda mezcla hechos y fantasía', 1),
       (11, 'No hay ninguna diferencia', 0),
       (11, 'La leyenda siempre es más verdadera que el relato', 0),
       (11, 'El relato histórico es más corto que la leyenda', 0);

-- Pregunta 12: Afirmación tiempo
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (12, 'Son momentos que no se relacionan entre sí', 0),
       (12, 'El pasado influye en el presente y el futuro', 1),
       (12, 'El futuro determina el pasado', 0),
       (12, 'El presente no tiene relación con el pasado', 0);

-- ---- HISTORIA BLOQUE 2 (preguntas 13..20) ----

INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (13, 'En grandes ciudades permanentes', 0),
       (13, 'En pequeños grupos nómadas', 1),
       (13, 'En fortalezas de piedra', 0),
       (13, 'En aldeas agrícolas', 0),
       (14, 'Construir templos', 0),
       (14, 'Cazar y recolectar alimentos', 1),
       (14, 'Hacer comercio de metales', 0),
       (14, 'Domesticar caballos', 0),
       (15, 'El descubrimiento de la rueda', 0),
       (15, 'El inicio de la agricultura', 1),
       (15, 'La llegada de los españoles', 0),
       (15, 'El descubrimiento del fuego', 0),
       (16, 'Trigo', 0),
       (16, 'Arroz', 0),
       (16, 'Maíz', 1),
       (16, 'Papa', 0),
       (17, 'Preclásico', 0),
       (17, 'Etapa Lítica', 1),
       (17, 'Clásico', 0),
       (17, 'Posclásico', 0),
       (18, 'Mayas', 0),
       (18, 'Mexicas', 0),
       (18, 'Toltecas', 0),
       (18, 'Olmecas', 1),
       (19, 'Canales de riego', 0),
       (19, 'Cabezas colosales de piedra', 1),
       (19, 'Murallas defensivas', 0),
       (19, 'Grandes caminos de piedra', 0),
       (20, 'Nomadismo y caza', 0),
       (20, 'Formación de grandes ciudades y avances culturales', 1),
       (20, 'Desaparición de todas las culturas', 0),
       (20, 'Uso exclusivo de cuevas como viviendas', 0);

-- ---- HISTORIA BLOQUE 3 (preguntas 21..28) ----

INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (21, 'Valle de Oaxaca', 0),
       (21, 'Valle de México', 1),
       (21, 'Valle de Puebla', 0),
       (21, 'Valle de Morelos', 0),
       (22, 'Pirámide del Sol', 0),
       (22, 'Pirámide de la Luna', 0),
       (22, 'Templo de Kukulkán', 1),
       (22, 'Calzadas ceremoniales', 0),
       (23, 'Mexicas', 0),
       (23, 'Teotihuacanos', 0),
       (23, 'Mayas', 1),
       (23, 'Zapotecas', 0),
       (24, 'Base 10', 0),
       (24, 'Base 12', 0),
       (24, 'Base 20', 1),
       (24, 'Base 60', 0),
       (25, 'Mayas', 0),
       (25, 'Teotihuacanos', 0),
       (25, 'Toltecas', 0),
       (25, 'Mexicas', 1),
       (26, 'Terrazas', 0),
       (26, 'Chinampas', 1),
       (26, 'Campos elevados', 0),
       (26, 'Cenotes', 0),
       (27, 'Tlatoanis', 0),
       (27, 'Pochtecas', 0),
       (27, 'Macehualtin', 1),
       (27, 'Pipiltin', 0),
       (28, 'Teotihuacán', 0),
       (28, 'Tikal', 0),
       (28, 'Tenochtitlan', 1),
       (28, 'Chichén Itzá', 0);

-- ---- HISTORIA BLOQUE 4 (preguntas 29..36) ----

INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (29, '1517', 0),
       (29, '1518', 0),
       (29, '1519', 1),
       (29, '1521', 0),
       (30, 'Pedro de Alvarado', 0),
       (30, 'Diego Velázquez', 1),
       (30, 'Carlos V', 0),
       (30, 'Francisco de Montejo', 0),
       (31, 'Zapotecas', 0),
       (31, 'Totonacas', 0),
       (31, 'Tlaxcaltecas', 1),
       (31, 'Mayas', 0),
       (32, 'Itzcóatl', 0),
       (32, 'Cuauhtémoc', 0),
       (32, 'Moctezuma II', 1),
       (32, 'Nezahualcóyotl', 0),
       (33, 'El regreso de Cortés a España', 0),
       (33, 'La creación del Virreinato', 0),
       (33, 'La Noche Triste', 1),
       (33, 'La fundación de Veracruz', 0),
       (34, 'El mito de Huitzilopochtli', 0),
       (34, 'La leyenda de Quetzalcoatl', 1),
       (34, 'La profecía de Tezcatlipoca', 0),
       (34, 'El Popol Vuh', 0),
       (35, 'La muerte de Moctezuma II', 0),
       (35, 'El primer contacto en Yucatán', 0),
       (35, 'La caída de Tenochtitlán el 13 de agosto de 1521', 1),
       (35, 'La firma del Tratado de Tordesillas', 0),
       (36, 'La organización imperial', 0),
       (36, 'La esclavitud ritual', 0),
       (36, 'Las lenguas y tradiciones', 1),
       (36, 'El tributo al tlatoani', 0);
-- ---- HISTORIA BLOQUE 5 (preguntas 37..44) ----
INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (37, 'Veracruz', 0),
       (37, 'Tenochtitlán', 0),
       (37, 'Ciudad de México', 1),
       (37, 'Guadalajara', 0),
       (38, 'Mestizos', 0),
       (38, 'Criollos', 0),
       (38, 'Peninsulares', 1),
       (38, 'Indígenas', 0),
       (39, 'Jesuitas y benedictinos', 0),
       (39, 'Franciscanos, dominicos y agustinos', 1),
       (39, 'Carmelitas', 0),
       (39, 'Trinitarios', 0),
       (40, 'Cobre', 0),
       (40, 'Oro y plata', 1),
       (40, 'Hierro', 0),
       (40, 'Carbón', 0),
       (41, 'Granjas familiares', 0),
       (41, 'Sistemas de tributo y trabajo indígena', 1),
       (41, 'Conventos misioneros', 0),
       (41, 'Templos religiosos', 0),
       (42, 'Trigo y cebada', 0),
       (42, 'Azúcar y cacao', 1),
       (42, 'Algodón y café', 0),
       (42, 'Papa y soya', 0),
       (43, 'Eliminó las costumbres indígenas', 0),
       (43, 'Generó una nueva identidad cultural', 1),
       (43, 'Fue rechazado por los colonizadores', 0),
       (43, 'Produjo conflictos étnicos permanentes', 0),
       (44, 'Hijos de indígenas y españoles', 0),
       (44, 'Indígenas evangelizados', 0),
       (44, 'Hijos de españoles nacidos en América', 1),
       (44, 'Esclavos africanos liberados', 0);

-- ---- HISTORIA BLOQUE 6 (preguntas 45..51) ----

INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (45, 'La guerra de los pasteles', 0),
       (45, 'La Revolución Francesa', 1),
       (45, 'La guerra de reforma', 0),
       (45, 'El porfiriato', 0),
       (46, 'José María Morelos', 0),
       (46, 'Vicente Guerrero', 0),
       (46, 'Miguel Hidalgo', 1),
       (46, 'Agustín de Iturbide', 0),
       (47, 'Tratados de Córdoba', 0),
       (47, 'Grito de Dolores', 1),
       (47, 'Plan de Ayutla', 0),
       (47, 'Congreso de Chilpancingo', 0),
       (48, 'Agustín de Iturbide', 0),
       (48, 'José María Morelos', 1),
       (48, 'Benito Juárez', 0),
       (48, 'Juan Aldama', 0),
       (49, 'La Constitución de 1857', 0),
       (49, 'La independencia de México', 1),
       (49, 'El Plan de Iguala', 0),
       (49, 'La monarquía española', 0),
       (50, 'Grito de Dolores', 0),
       (50, 'Tratados de Córdoba', 1),
       (50, 'Plan de Tuxtepec', 0),
       (50, 'Plan de Ayala', 0),
       (51, 'Ejército de Oriente', 0),
       (51, 'Ejército Realista', 0),
       (51, 'Ejército Trigarante', 1),
       (51, 'Ejército del Norte', 0);

-- ---- HISTORIA BLOQUE 7 (preguntas 52..59) ----

INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (52, 'Benito Juárez', 0),
       (52, 'Maximiliano de Habsburgo', 0),
       (52, 'Agustín de Iturbide', 1),
       (52, 'Antonio López de Santa Anna', 0),
       (53, 'Una monarquía constitucional', 0),
       (53, 'Un gobierno centralista', 0),
       (53, 'Una república federal', 1),
       (53, 'El segundo imperio', 0),
       (54, 'Benito Juárez', 0),
       (54, 'Miguel Hidalgo', 0),
       (54, 'Porfirio Díaz', 0),
       (54, 'Antonio López de Santa Anna', 1),
       (55, 'México anexó Texas', 0),
       (55, 'México perdió más de la mitad de su territorio', 1),
       (55, 'Se abolió la esclavitud', 0),
       (55, 'Terminó la Guerra de Reforma', 0),
       (56, 'Intervención Francesa', 0),
       (56, 'Guerra de Reforma', 0),
       (56, 'Guerra de los Pasteles', 1),
       (56, 'Batalla de Puebla', 0),
       (57, 'Derechos divinos del rey', 0),
       (57, 'Supremacía del clero', 0),
       (57, 'Derechos individuales y libertad de expresión', 1),
       (57, 'Reelección presidencial indefinida', 0),
       (58, 'Agustín de Iturbide', 0),
       (58, 'Sebastián Lerdo', 0),
       (58, 'Maximiliano de Habsburgo', 1),
       (58, 'Napoleón III', 0),
       (59, 'Plan de Ayutla', 0),
       (59, 'Plan de San Luis', 0),
       (59, 'Plan de Tuxtepec', 1),
       (59, 'Plan de Tacubaya', 0);

-- ---- HISTORIA BLOQUE 8 (preguntas 60..67) ----

INSERT INTO tbl_opcion (id_pregunta, texto, es_correcta)
VALUES (60, '10 años', 0),
       (60, '25 años', 0),
       (60, '30 años', 1),
       (60, '5 años', 0),
       (61, 'Nacionalización del petróleo', 0),
       (61, 'Reforma agraria', 0),
       (61, 'Construcción de más de 19,000 km de vías férreas', 1),
       (61, 'Reducción del poder militar', 0),
       (62, 'Pluralidad democrática', 0),
       (62, 'Alternancia política', 0),
       (62, 'Censura y represión', 1),
       (62, 'Transparencia electoral', 0),
       (63, 'Plan de Ayala', 0),
       (63, 'Plan de Tuxtepec', 0),
       (63, 'Plan de San Luis', 1),
       (63, 'Plan de Guadalupe', 0),
       (64, 'La promulgación de la Constitución de 1917', 0),
       (64, 'El levantamiento armado del 20 de noviembre de 1910', 1),
       (64, 'La Decena Trágica', 0),
       (64, 'El exilio de Porfirio Díaz', 0),
       (65, 'El derrocamiento de Porfirio Díaz', 0),
       (65, 'La renuncia de Carranza', 0),
       (65, 'El asesinato de Madero y Pino Suárez', 1),
       (65, 'La unión de Villa y Zapata', 0),
       (66, 'Carranza y Obregón', 0),
       (66, 'Zapata y Madero', 0),
       (66, 'Villa y Zapata', 1),
       (66, 'Huerta y Calles', 0),
       (67, 'Reelección indefinida', 0),
       (67, 'Supremacía de la Iglesia', 0),
       (67, 'Derechos laborales y propiedad de la tierra', 1),
       (67, 'Retorno al centralismo', 0);

-- ============================================================
-- VERIFICACIÓN FINAL
-- ============================================================

SELECT 'Script ejecutado correctamente' AS mensaje;
SELECT COUNT(*) AS total_materias
FROM tbl_materia;
SELECT COUNT(*) AS total_temas
FROM tbl_tema;
SELECT COUNT(*) AS total_subtemas
FROM tbl_subtema;
SELECT COUNT(*) AS total_ejercicios
FROM tbl_ejercicio;
SELECT COUNT(*) AS total_preguntas
FROM tbl_pregunta;
SELECT COUNT(*) AS total_opciones
FROM tbl_opcion;
SELECT COUNT(*) AS total_recursos
FROM tbl_recurso_adjunto;
SELECT COUNT(*) AS total_usuarios
FROM tbl_usuario;