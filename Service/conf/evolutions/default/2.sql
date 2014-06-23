# add Sessions

# --- !Ups


CREATE TABLE user_sessions (
    id character varying(254) NOT NULL,
    user_id character varying NOT NULL
);

ALTER TABLE ONLY user_sessions
    ADD CONSTRAINT session_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_sessions ADD PRIMARY KEY( "id" );

# --- !Downs

