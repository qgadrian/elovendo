---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- Eliminamos tablas.
    DROP TABLE IF EXISTS item CASCADE;
    DROP TABLE IF EXISTS subcategory CASCADE;
    DROP TABLE IF EXISTS category CASCADE;
	DROP TABLE IF EXISTS votes CASCADE;
	DROP TABLE IF EXISTS userprofile CASCADE;

---

-- USER TABLE
	CREATE TABLE userprofile (
		  userid BIGINT NOT NULL AUTO_INCREMENT,
		  login VARCHAR(20) NOT NULL,
		  password VARCHAR(200),
		  firstname VARCHAR(20) NOT NULL,
		  lastname VARCHAR(20) NOT NULL,
		  address VARCHAR(50),
		  phone VARCHAR(20),
		  email VARCHAR(20) NOT NULL,
		  votespositive INT NOT NULL DEFAULT 0,
		  votesnegative INT NOT NULL DEFAULT 0,
		  role VARCHAR(20) NOT NULL,
		  enabled BOOLEAN NOT NULL,
		  sign_in_provider VARCHAR(20),
		  CONSTRAINT pk_userid PRIMARY KEY (userid),
		  CONSTRAINT u_login UNIQUE(login),
		  CONSTRAINT check_votespositive CHECK (votespositive > 0),
		  CONSTRAINT check_votesnegative CHECK (votesnegative > 0)
	);

-- INSERT INTO userprofile VALUES (NULL, 'charlieHarper', 'password', 'Charlie', 'Harper', 'charlie address', 'charliephone', 'charlieemail', 'ROLE_USER', NULL);

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
            CONSTRAINT fk_subcategory_categoryid FOREIGN KEY (categoryid) REFERENCES category(categoryid) ON UPDATE CASCADE ON DELETE CASCADE
    );

    -- ITEM TABLE

    	CREATE TABLE item (
    	    itemid BIGINT NOT NULL AUTO_INCREMENT,
    	    userid BIGINT NOT NULL,
    	    subcategoryid BIGINT NOT NULL,
    	    title VARCHAR(20) NOT NULL,
    	    description VARCHAR(250) NOT NULL,
    	    prize DECIMAL(6,2) NOT NULL,
    	    startdate DATETIME NOT NULL,
    	    CONSTRAINT pk_itemid PRIMARY KEY (itemid),
    	    CONSTRAINT fk_item_userid FOREIGN KEY (userid) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
    	    CONSTRAINT fk_item_subcategoryid FOREIGN KEY (subcategoryid) REFERENCES subcategory(subcategoryid) ON UPDATE CASCADE ON DELETE CASCADE
    	);

    -- INSERT INTO item VALUES (NULL, 1, 'PS4', 'Play station 4', 200.5, NOW());

-- VOTEs

    CREATE TABLE votes (
        userid BIGINT NOT NULL,
        votetype TINYINT(1),
        votemessage VARCHAR(20),
        CONSTRAINT fk_votes_userid FOREIGN KEY (userid) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE
    );