---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- ROLES

INSERT INTO role VALUES (1, 'ROLE_USER');
INSERT INTO role VALUES (2, 'ROLE_ADMIN');

-- USER
-- avatar uservalue, points, registerDate, roleid, you're welcome
INSERT INTO userprofile VALUES(1, 'Admin', '$2a$10$LjDlebedYsrYKAMrZC4thu2mRmEeWCGJ76FN6Kt0v3FxgO8bc.VIa', 'Admin', 'admin', NULL, NULL, 0, 'elovendogod', 'images/default/profile/default', 100, 999, NOW(), 2, TRUE, NULL);

-- CATEGORY

INSERT INTO category VALUES (1, 'Tecnología');
INSERT INTO category VALUES (2, 'Deportes');
INSERT INTO category VALUES (3, 'Hogar');
INSERT INTO category VALUES (4, 'Motor');
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

INSERT INTO subcategory VALUES(12, 3, 'Sofás');
INSERT INTO subcategory VALUES(13, 3, 'Electrodomésticos');
INSERT INTO subcategory VALUES(14, 3, 'Muebles');
INSERT INTO subcategory VALUES(15, 3, 'Jardín');
INSERT INTO subcategory VALUES(16, 3, 'Otros');

INSERT INTO subcategory VALUES(17, 4, 'Coches');
INSERT INTO subcategory VALUES(18, 4, 'Motos');
INSERT INTO subcategory VALUES(19, 4, 'Náutica');
INSERT INTO subcategory VALUES(20, 4, 'Remolques');
INSERT INTO subcategory VALUES(22, 4, 'Neumáticos');
INSERT INTO subcategory VALUES(23, 4, 'Accesorios coches');
INSERT INTO subcategory VALUES(24, 4, 'Accesorios motos');
INSERT INTO subcategory VALUES(25, 4, 'Otros');


-- ITEMS

--INSERT INTO item VALUES (NULL, 1, 2, 'PS4 + 2 mandos + Juegos', 'Play station 4 original, comprada en Marruecos, está un poco rayada.', 1, 200, NOW(), null);
--INSERT INTO item VALUES (NULL, 2, 2, 'XBOX ONE + 3 mandos', 'Original, se vende porque me compré una gameboy', 2, 250.5, NOW(), null);
--INSERT INTO item VALUES (NULL, 1, 3, 'PC 3ghz + 8GB RAM Nvidia 964GTZ', 'Está como nuevo, vendo por no usar', 1, 500.5, NOW(), null);
--INSERT INTO item VALUES (NULL, 1, 4, 'Televisión 20 pulgadas', 'Un televisor grande y con muy buena pantalla', 2, 1200, NOW(), null);