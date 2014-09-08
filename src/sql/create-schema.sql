---------------------------------------------------
-- Sentencias de creación de tablas y secuencias --
---------------------------------------------------

-- Eliminamos tablas.
	/*DROP TABLE IF EXISTS image CASCADE*/;
	DROP TABLE IF EXISTS messageState CASCADE;
	DROP TABLE IF EXISTS message CASCADE;
	DROP TABLE IF EXISTS messageThread CASCADE;
	DROP TABLE IF EXISTS vote CASCADE;
	DROP TABLE IF EXISTS purchase CASCADE;
    DROP TABLE IF EXISTS item CASCADE;
    DROP TABLE IF EXISTS subcategory CASCADE;
    DROP TABLE IF EXISTS category CASCADE;
	DROP TABLE IF EXISTS userprofile CASCADE;
	DROP TABLE IF EXISTS role CASCADE;
	DROP TABLE IF EXISTS province CASCADE;

---

-- PROVINCE
--CREATE TABLE province (
	--provinceId BIGINT NOT NULL AUTO_INCREMENT,
	--provinceName VARCHAR(20),
	--CONSTRAINT pk_provinceId PRIMARY KEY (provinceId),
	--CONSTRAINT province_u_provinceName UNIQUE(provinceName)
--);

-- ROLE

	CREATE TABLE role (
		roleid BIGINT NOT NULL AUTO_INCREMENT,
		rolename VARCHAR(50) NOT NULL,
		CONSTRAINT pk_roleid PRIMARY KEY (roleid),
		CONSTRAINT u_rolename UNIQUE(rolename)
	);	
	
-- USER TABLE
--TODO: save register date to see how long is member
	CREATE TABLE userprofile (
		  userid BIGINT NOT NULL AUTO_INCREMENT,
		  login VARCHAR(15) NOT NULL,
		  password VARCHAR(200),
		  firstname VARCHAR(20) NOT NULL,
		  lastname VARCHAR(20) NOT NULL,
		  address VARCHAR(100),
		  phone VARCHAR(20),
		  email VARCHAR(255) NOT NULL,
		  avatar VARCHAR(255),
		  userValue INT NOT NULL DEFAULT 0,
		  points INT NOT NULL DEFAULT 0,
		  registerDate DATETIME NOT NULL,
		  roleid BIGINT NOT NULL,
		  enabled BOOLEAN NOT NULL,
		  sign_in_provider VARCHAR(20),
		  CONSTRAINT pk_userid PRIMARY KEY (userid),
		  CONSTRAINT u_login UNIQUE(login),
		  CONSTRAINT fk_user_roleid FOREIGN KEY (roleid) REFERENCES role(roleid) ON UPDATE CASCADE ON DELETE CASCADE,
		  CONSTRAINT check_votespositive CHECK (votespositive > 0),
		  CONSTRAINT check_votesnegative CHECK (votesnegative > 0),
		  CONSTRAINT check_points CHECK (points > 0),
		  CONSTRAINT check_uservalue_min CHECK (userValue > 0),
		  CONSTRAINT check_uservalue_max CHECK (userValue < 101)
	);

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
    	    title VARCHAR(40) NOT NULL,
    	    description TEXT NOT NULL,
    	    prize DECIMAL(6,2) NOT NULL,
    	    startdate DATETIME NOT NULL,
    	    endDate DATETIME NOT NULL,
    	    mainImage VARCHAR(255),
    	    image1 VARCHAR(255),
    	    image2 VARCHAR(255),
    	    image3 VARCHAR(255),
    	    youtubeVideo VARCHAR(255),
    	    featured BOOLEAN NOT NULL,
    	    highlight BOOLEAN NOT NULL,
    	    autoRenew BOOLEAN NOT NULL,
    	    latitude DOUBLE NOT NULL,
    	    longitude DOUBLE NOT NULL,
    	    cosRadLat DOUBLE NOT NULL,
    	    sinRadLat DOUBLE NOT NULL,
    	    radLng TEXT NOT NULL,
    	    CONSTRAINT pk_item PRIMARY KEY (itemid),
    	    CONSTRAINT fk_item_userid FOREIGN KEY (userid) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
    	    CONSTRAINT fk_item_subcategoryid FOREIGN KEY (subcategoryid) REFERENCES subcategory(subcategoryid) ON UPDATE CASCADE ON DELETE CASCADE
    	);

    -- IMAGES
    -- TODO: Delte this stuff
	/*CREATE TABLE image (
		itemid BIGINT NOT NULL,
		imghome BLOB,
		img2 BLOB,
		img3 BLOB,
		img4 BLOB,
		img5 BLOB,
		img6 BLOB,
		CONSTRAINT pk_image_itemid PRIMARY KEY (itemid)
	);*/

-- VOTE

    CREATE TABLE vote (
    	voteId BIGINT NOT NULL AUTO_INCREMENT,
        userIdVote BIGINT NOT NULL,
        userIdReceive BIGINT NOT NULL,
        itemId BIGINT NOT NULL,
        voteType TINYINT(1),
        voteState BOOLEAN,
        voteValue INTEGER NOT NULL,
        voteMessage VARCHAR(20),
        CONSTRAINT pk_vote PRIMARY KEY (voteId),
        CONSTRAINT fk_vote_useridvote FOREIGN KEY (userIdVote) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT fk_vote_useridreceive FOREIGN KEY (userIdReceive) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT fk_vote_itemId FOREIGN KEY (itemId) REFERENCES item(itemid) ON UPDATE CASCADE ON DELETE CASCADE
    );
    
-- PENDING VOTE

/*
CREATE TABLE pendingVote(
	pendingBoteId BIGINT NOT NULL AUTOINCREMENT,
	userIdPending BIGINT NOT NULL,
	userIdVoted BIGINT NOT NULL,
);
*/

-- PAYMENTS
-- txn_id -> Transaction id
-- payment_date
-- payment_status
-- item_name
-- item_number -> USER ID
-- ipn_track_id -< for paypal's internal purposes, save it for, "who knows"
-- mc_gross -> Precio
-- mc_fee -> Paypal commision
-- receiver_id -> ID de recibo de usuario
-- first_name
-- last_name
-- payer_email
-- residence_country -> Country

    CREATE TABLE purchase (
		txn_id VARCHAR(255),
		payment_date DATETIME NOT NULL,
		payment_status VARCHAR(100),
		item_name VARCHAR(100),
		item_number INTEGER,
		userid BIGINT NOT NULL,
		ipn_track_id VARCHAR(255),
		mc_gross DECIMAL(6,2) NOT NULL,
		mc_fee DECIMAL(6,2) NOT NULL,
		receiver_id VARCHAR(255),
		first_name VARCHAR(100),
		last_name VARCHAR(100),
		payer_email VARCHAR(100),
		residence_country VARCHAR(100),
		CONSTRAINT pk_purchase_txn_id PRIMARY KEY (txn_id),
		CONSTRAINT fk_purchase_userid FOREIGN KEY (userid) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
		CONSTRAINT check_purchase_item_number CHECK (item_number > 0)
	);

    
    
-- MESSAGING

	CREATE TABLE messageThread(
		messageThreadId BIGINT NOT NULL AUTO_INCREMENT,
		participant1 BIGINT NOT NULL,
		participant2 BIGINT NOT NULL,
		CONSTRAINT pk_messThreadId PRIMARY KEY(messageThreadId),
		CONSTRAINT fk_user_user1 FOREIGN KEY (participant1) REFERENCES userprofile(userid),
		CONSTRAINT fk_user_user2 FOREIGN KEY (participant2) REFERENCES userprofile(userid)
	);
	
	CREATE TABLE message (
		messageId BIGINT NOT NULL AUTO_INCREMENT,
		messageThreadId BIGINT NOT NULL,
		senderId BIGINT NOT NULL,
		text VARCHAR(255) NOT NULL,
		messageDate DATETIME NOT NULL,
		ipAddress BIGINT NOT NULL,
		CONSTRAINT pk_messageId PRIMARY KEY(messageId),
		CONSTRAINT fk_user_senderId FOREIGN KEY (senderId) REFERENCES userprofile(userid)
	);
	
	CREATE TABLE messageState(
		messageId BIGINT NOT NULL,
		userId BIGINT NOT NULL,
		messageThreadId BIGINT NOT NULL,
		readState BOOLEAN NOT NULL,
		CONSTRAINT fk_message_messageId FOREIGN KEY (messageId) REFERENCES message(messageId),
		CONSTRAINT fk_user_userId FOREIGN KEY (userId) REFERENCES userprofile(userid),
		CONSTRAINT fk_messageThread_messageThreadId FOREIGN KEY (messageThreadId) REFERENCES messageThread(messageThreadId)
	);
    
    /*CREATE TABLE conversation(
    	conversationid BIGINT NOT NULL AUTOINCREMENT,
    	userid1 BIGINT NOT NULL,
    	userid2 BIGINT NOT NULL,
    	CONSTRAINT pk_conversationid PRIMARY KEY (conversationid),
		CONSTRAINT fk_conversation_userid1 FOREIGN KEY (userid1) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
		CONSTRAINT fk_conversation_userid2 FOREIGN KEY (userid2) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
    );
    
    CREATE TABLE conversationreply(
    	conversationreplyid BIGINT NOT NULL AUTOINCREMENT,
    	reply BLOB NOT NULL,
    	userid BIGINT NOT NULL,
    	ip VARCHAR(30) NOT NULL,
    	msgdate DATETIME NOT NULL,
    	conversationid BIGINT NOT NULL,
    	CONSTRAINT pk_conversationid PRIMARY KEY (conversationid),
		CONSTRAINT fk_convreply_userid FOREIGN KEY (userid) REFERENCES userprofile(userid) ON UPDATE CASCADE ON DELETE CASCADE,
		CONSTRAINT fk_convreply_convid FOREIGN KEY (conversationid) REFERENCES conversation(conversationid) ON UPDATE CASCADE ON DELETE CASCADE,
    );*/