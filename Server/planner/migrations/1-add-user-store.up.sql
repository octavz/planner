CREATE TABLE users
(id VARCHAR(40) PRIMARY KEY,
 login VARCHAR(30),
 openid_type INT,
 openid_token VARCHAR(100),
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP,
 last_login TIMESTAMP,
 status INT default 0,
 password VARCHAR(100));

CREATE TABLE oauth_tokens
(id VARCHAR(200) PRIMARY KEY,
 user_id VARCHAR(40) REFERENCES users(id),
 subject INT,
 expires TIMESTAMP,
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP,
 scope VARCHAR(1000),
 object VARCHAR(1000));

CREATE TABLE oauth_clients
(id VARCHAR(40) PRIMARY KEY,
 secret VARCHAR(100),
 name VARCHAR(255),
 url VARCHAR(255),
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP );

CREATE TABLE oauth_codes
(id VARCHAR(200) PRIMARY KEY,
 client_id VARCHAR(40) REFERENCES oauth_clients(id),
 subject INT,
 redirect_uri VARCHAR(255),
 expires TIMESTAMP,
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP,
 scope VARCHAR(1000),
 object VARCHAR(1000));

