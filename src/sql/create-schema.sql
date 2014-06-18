---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- Eliminamos tablas.
	DROP TABLE IF EXISTS userprofile CASCADE;
---

-- USER TABLE
	CREATE TABLE userprofile (
		  userid BIGINT NOT NULL AUTO_INCREMENT,
		  login VARCHAR(20) NOT NULL,
		  password BLOB,
		  firstname VARCHAR(20) NOT NULL,
		  lastname VARCHAR(20) NOT NULL,
		  address VARCHAR(50),
		  phone VARCHAR(20),
		  email VARCHAR(20) NOT NULL,
		  role VARCHAR(20) NOT NULL,
		  sign_in_provider VARCHAR(20),
		  CONSTRAINT pk_userid PRIMARY KEY (userid),
		  CONSTRAINT u_login UNIQUE(login)
	);

-- INSERT INTO userprofile VALUES (NULL, 'charlieHarper', 'password', 'Charlie', 'Harper', 'charlie address', 'charliephone', 'charlieemail', 'ROLE_USER', NULL);

-- ITEM TABLE

	CREATE TABLE item (
	    itemid BIGINT NOT NULL AUTO_INCREMENT,
	    userid BIGINT NOT NULL,
	    title VARCHAR(20) NOT NULL,
	    description VARCHAR(250) NOT NULL,
	    prize DECIMAL(5,2) NOT NULL,
	    startdate DATETIME NOT NULL,
	    CONSTRAINT pk_itemid PRIMARY KEY (itemid),
	    CONSTRAINT fk_userid FOREIGN KEY (userid) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE
	);

-- INSERT INTO item VALUES (NULL, 1, 'PS4', 'Play station 4', 200.5, NOW());

-- CATEGORY

    CREATE TABLE category (
        categoryid BIGINT NOT NULL AUTO_INCREMENT,
        categoryname VARCHAR(20) NOT NULL,
        CONSTRAINT pk_categoryid PRIMARY KEY (categoryid),
        CONSTRAINT u_categoryname UNIQUE(categoryname)
    );

-- SUBCATEGORY

    CREATE TABLE subcategory (
            subcategoryid BIGINT NOT NULL AUTO_INCREMENT,
            categoryid BIGINT NOT NULL,
            subcategoryname VARCHAR(20) NOT NULL,
            CONSTRAINT pk_subcategoryid PRIMARY KEY (subcategoryid),
            CONSTRAINT u_subcategoryname UNIQUE(subcategoryname),
            CONSTRAINT fk_categoryid FOREIGN KEY (categoryid) REFERENCES category(categoryid) ON UPDATE CASCADE ON DELETE CASCADE
    );