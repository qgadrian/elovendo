---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- Eliminamos tablas.
	DROP TABLE IF EXISTS userprofile CASCADE;
---

-- USER TABLE
	CREATE TABLE userprofile (
		  userid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		  login VARCHAR(20) UNIQUE NOT NULL,
		  firstname VARCHAR(20) NOT NULL,
		  lastname VARCHAR(20) NOT NULL,
		  address VARCHAR(50),
		  phone VARCHAR(20),
		  email VARCHAR(20) NOT NULL
	);

	--password BLOB,

-- ITEM TABLE

	CREATE TABLE item (
	    itemid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	    title VARCHAR(20) NOT NULL,
	    description VARCHAR(250) NOT NULL,
	    prize DECIMAL(5,2) NOT NULL,
	    startdate DATETIME NOT NULL
	);