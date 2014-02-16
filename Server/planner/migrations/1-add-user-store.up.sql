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

CREATE TABLE oauth_clients
(id VARCHAR(40) PRIMARY KEY,
 secret VARCHAR(100),
 name VARCHAR(255),
 url VARCHAR(255),
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP );

CREATE TABLE oauth_tokens
(id VARCHAR(200) PRIMARY KEY,
 user_id VARCHAR(40) REFERENCES users(id),
 client_id VARCHAR(40) REFERENCES oauth_clients(id),
 expires TIMESTAMP,
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP,
 scope VARCHAR(1000),
 object VARCHAR(1000));


CREATE TABLE oauth_codes
(id VARCHAR(200) PRIMARY KEY,
 client_id VARCHAR(40) REFERENCES oauth_clients(id),
 user_id VARCHAR(40) REFERENCES users(id),
 redirect_uri VARCHAR(255),
 expires TIMESTAMP,
 created TIMESTAMP DEFAULT now(),
 updated TIMESTAMP,
 scope VARCHAR(1000),
 object VARCHAR(1000));

insert into oauth_clients(id,secret,name) values('1', 'secret','main web app');
insert into users(id,login,openid_token,openid_type,password) values('1','test@test-planner.com',
        'test@test-planner.com',0,'$2a$10$54yLfhWzYsxZnZR6wk8G9.8PLQXY5aSps.M0TJ1cNRg00kSP14Mku') 
