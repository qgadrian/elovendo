---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- USER

INSERT INTO userprofile VALUES (1, 'charlieHarper', 'password', 'Charlie', 'Harper', 'charlie address', 'charliephone', 'charlieemail', 0, 0, 'ROLE_USER', true, NULL);

-- CATEGORY

INSERT INTO category VALUES (1, 'Tecnología');
INSERT INTO category VALUES (2, 'Deportes');
INSERT INTO category VALUES (3, 'Hogar');
INSERT INTO category VALUES (4, 'Coches');
INSERT INTO category VALUES (5, 'Mascotas');
INSERT INTO category VALUES (6, 'Libros');
INSERT INTO category VALUES (7, 'Moda');
INSERT INTO category VALUES (8, 'Joyería');
INSERT INTO category VALUES (9, 'Niños');
INSERT INTO category VALUES (10, 'Música');

-- SUBCATEGORY

INSERT INTO subcategory VALUES(1, 1, 'Móviles');
INSERT INTO subcategory VALUES(2, 1, 'Videoconsolas');
INSERT INTO subcategory VALUES(3, 1, 'Informática');
INSERT INTO subcategory VALUES(4, 1, 'Imagen');
INSERT INTO subcategory VALUES(5, 1, 'Sonido');
INSERT INTO subcategory VALUES(6, 1, 'Fotografía');

INSERT INTO subcategory VALUES(7, 2, 'Relojes GPS');
INSERT INTO subcategory VALUES(8, 2, 'Ropa');
INSERT INTO subcategory VALUES(9, 2, 'Zapatillas');
INSERT INTO subcategory VALUES(10, 2, 'Bicicletas');
INSERT INTO subcategory VALUES(11, 2, 'Accesorios');


-- ITEMS

INSERT INTO item VALUES (NULL, 1, 2, 'PS4', 'Play station 4', 200, NOW());
INSERT INTO item VALUES (NULL, 1, 2, 'XBOX ONE', 'De Microsoft', 250.5, NOW());
INSERT INTO item VALUES (NULL, 1, 3, 'PC', 'Nuevo', 500.5, NOW());
INSERT INTO item VALUES (NULL, 1, 4, 'TV', 'Muy grande', 1200, NOW());