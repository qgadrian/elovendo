---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- PROVINCE
INSERT INTO province VALUES (1, 'A Coruña');
INSERT INTO province VALUES (2, 'Barcelona');
INSERT INTO province VALUES (3, 'Madrid');
INSERT INTO province VALUES (4, 'Lugo');


-- ROLES

INSERT INTO role VALUES (1, 'ROLE_USER');

-- USER
-- prvince, avatar uservalue, points, roleid, you're welcome
INSERT INTO userprofile VALUES (1, 'usuario', 'password', 'Charlie', 'Harper', 'charlie address', 'charliephone', 'charlieemail', 1, NULL, 0, 0, 1, true, NULL);
INSERT INTO userprofile VALUES (2, 'user', 'password', 'User', 'LastName', 'user address', 'userphone', 'userEmail', 2, NULL, 0, 0, 1, true, NULL);

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

INSERT INTO subcategory VALUES(1, 1, 'Moviles');
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

--INSERT INTO item VALUES (NULL, 1, 2, 'PS4 + 2 mandos + Juegos', 'Play station 4 original, comprada en Marruecos, está un poco rayada.', 1, 200, NOW(), null);
--INSERT INTO item VALUES (NULL, 2, 2, 'XBOX ONE + 3 mandos', 'Original, se vende porque me compré una gameboy', 2, 250.5, NOW(), null);
--INSERT INTO item VALUES (NULL, 1, 3, 'PC 3ghz + 8GB RAM Nvidia 964GTZ', 'Está como nuevo, vendo por no usar', 1, 500.5, NOW(), null);
--INSERT INTO item VALUES (NULL, 1, 4, 'Televisión 20 pulgadas', 'Un televisor grande y con muy buena pantalla', 2, 1200, NOW(), null);