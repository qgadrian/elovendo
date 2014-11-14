CREATE TABLE ItemReport(itemReportId BIGINT NOT NULL AUTO_INCREMENT, 
itemId BIGINT NOT NULL,
reporterId BIGINT NOT NULL,
reportMessage VARCHAR(255),
CONSTRAINT pk_itemReport PRIMARY KEY (itemReportId),
CONSTRAINT fk_itemReport_reporterId FOREIGN KEY (reporterId) REFERENCES userprofile(userid),
CONSTRAINT fk_itemReport_itemId FOREIGN KEY (itemId) REFERENCES item(itemid));

CREATE TABLE UserReport(userReportId BIGINT NOT NULL AUTO_INCREMENT, 
userId BIGINT NOT NULL,
reporterId BIGINT NOT NULL,
reportMessage VARCHAR(255),
CONSTRAINT pk_userReport PRIMARY KEY (userReportId),
CONSTRAINT fk_userReport_reporterId FOREIGN KEY (reporterId) REFERENCES userprofile(userid),
CONSTRAINT fk_userReport_userId FOREIGN KEY (userId) REFERENCES userprofile(userid));