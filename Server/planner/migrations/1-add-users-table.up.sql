CREATE TABLE users
(id VARCHAR(20) PRIMARY KEY,
 email VARCHAR(30),
 openid_type INT,
 openid_value VARCHAR(100),
 created TIMESTAMP,
 updated TIMESTAMP,
 last_login TIMESTAMP,
 pass VARCHAR(100));
