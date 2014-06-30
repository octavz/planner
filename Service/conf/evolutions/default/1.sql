# Users schema
 
# --- !Ups

-- CREATE DATABASE planner WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';


CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';

CREATE TABLE access_tokens (
    access_token character varying(254) NOT NULL,
    refresh_token character varying(254),
    user_id character varying NOT NULL,
    scope character varying(254),
    expires_in integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
    client_id character varying(254) NOT NULL
);

CREATE TABLE actions (
    id character varying(40) NOT NULL,
    user_id character varying(40) NOT NULL,
    group_id character varying(40) NOT NULL,
    description character varying(100),
    url character varying(2000) NOT NULL,
    verb integer NOT NULL,
    secured smallint NOT NULL,
    perm_owner smallint DEFAULT 0 NOT NULL,
    perm_group smallint DEFAULT 0 NOT NULL,
    perm_public smallint DEFAULT 2 NOT NULL,
    created timestamp without time zone DEFAULT now(),
    updated timestamp without time zone DEFAULT now()
);

CREATE TABLE auth_codes (
    authorization_code character varying(254) NOT NULL,
    user_id character varying NOT NULL,
    redirect_uri character varying(254),
    created_at timestamp without time zone NOT NULL,
    scope character varying(254),
    client_id character varying(254) NOT NULL,
    expires_in integer NOT NULL
);

CREATE TABLE client_grant_types (
    client_id character varying(254) NOT NULL,
    grant_type_id integer NOT NULL
);

CREATE TABLE clients (
    id character varying(254) NOT NULL,
    secret character varying(254),
    redirect_uri character varying(254),
    scope character varying(254)
);

CREATE TABLE entity_types (
    id character varying(40) NOT NULL,
    description character varying(100)
);

CREATE TABLE extra_fields (
    id character varying(40) NOT NULL,
    name character varying(200) NOT NULL,
    validation character varying(1000),
    entity_type_id character varying(40) NOT NULL,
    parent_id character varying(40),
    plugin_id character varying(40) NOT NULL
);

CREATE TABLE extra_fields_values (
    id character varying(40) NOT NULL,
    extra_field_id character varying(40) NOT NULL,
    entity_id character varying(40) NOT NULL,
    value character varying(1000) NOT NULL
);

CREATE TABLE grant_types (
    id integer NOT NULL,
    grant_type character varying(254) NOT NULL
);


CREATE TABLE groups (
    id character varying(40) NOT NULL,
    project_id character varying(40) NOT NULL,
    type smallint DEFAULT 0 NOT NULL,
    name character varying(200) NOT NULL,
    created timestamp without time zone DEFAULT now(),
    updated timestamp without time zone DEFAULT now()
);

CREATE TABLE groups_users (
    group_id character varying(40) NOT NULL,
    user_id character varying(40) NOT NULL
);

CREATE TABLE labels (
    id character varying(40) NOT NULL,
    lang character varying(10) NOT NULL,
    entity_id character varying(40) NOT NULL,
    entity_type_id character varying(40) NOT NULL,
    label1 character varying(1000),
    label2 character varying(1000),
    label3 character varying(1000)
);

CREATE TABLE plugins (
    id character varying(40) NOT NULL,
    user_id character varying(40) NOT NULL,
    group_id character varying(40) NOT NULL,
    project_id character varying(40) NOT NULL,
    description text,
    perm_owner smallint DEFAULT 0 NOT NULL,
    perm_group smallint DEFAULT 0 NOT NULL,
    perm_public smallint DEFAULT 2 NOT NULL
);

CREATE TABLE plugins_projects (
    id character varying(40) NOT NULL,
    plugin_id character varying(40) NOT NULL,
    project_id character varying(40) NOT NULL,
    enabled smallint DEFAULT 1 NOT NULL
);

CREATE TABLE projects (
    id character varying(40) NOT NULL,
    user_id character varying(40) NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    parent_id character varying(40),
    status smallint DEFAULT 0 NOT NULL,
    perm_public smallint DEFAULT 2 NOT NULL,
    created timestamp without time zone DEFAULT now(),
    updated timestamp without time zone DEFAULT now()
);

CREATE TABLE resources (
    id character varying(40) NOT NULL,
    content character varying(1000),
    entity_type_id character varying(40) NOT NULL,
    user_id character varying(40) NOT NULL,
    group_id character varying(40) NOT NULL,
    perm_owner smallint DEFAULT 0 NOT NULL,
    perm_group smallint DEFAULT 0 NOT NULL,
    perm_public smallint DEFAULT 2 NOT NULL,
    created timestamp without time zone DEFAULT now(),
    updated timestamp without time zone DEFAULT now()
);

CREATE TABLE user_statuses (
    id integer NOT NULL,
    description character varying(100)
);

CREATE TABLE users (
    id character varying(40) NOT NULL,
    login character varying(255),
    openid_type integer DEFAULT 0 NOT NULL,
    openid_token character varying(255),
    created timestamp without time zone DEFAULT now(),
    updated timestamp without time zone DEFAULT now(),
    last_login timestamp without time zone,
    status integer DEFAULT 0 NOT NULL,
    password character varying(100)
);

CREATE TABLE verbs (
    id integer NOT NULL,
    description character varying(100)
);

INSERT INTO actions VALUES ('1', '1', '1', NULL, '/login', 1, 0, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('2', '1', '1', NULL, '/login', 2, 0, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('3', '1', '1', NULL, '/user', 1, 1, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('4', '1', '1', NULL, '/user', 3, 1, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('5', '1', '1', NULL, '/logout', 1, 1, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('6', '1', '1', NULL, '/register', 1, 0, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('7', '1', '1', NULL, '/project', 1, 0, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('8', '1', '1', NULL, '/projects', 2, 0, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO actions VALUES ('9', '1', '1', NULL, '/projects', 3, 0, 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO entity_types VALUES ('1', 'user');
INSERT INTO entity_types VALUES ('2', 'project');
INSERT INTO entity_types VALUES ('3', 'actions');
INSERT INTO entity_types VALUES ('4', 'client routes');

INSERT INTO groups VALUES ('1', '1', 1, 'admin', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups VALUES ('11', '1', 2, 'users', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups VALUES ('2', '2', 1, 'admin', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups VALUES ('21', '2', 2, 'users', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups VALUES ('3', '3', 1, 'admin', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups VALUES ('31', '3', 2, 'users', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO groups_users VALUES ('1', '1');
INSERT INTO groups_users VALUES ('11', '1');
INSERT INTO groups_users VALUES ('2', '1');
INSERT INTO groups_users VALUES ('21', '1');

INSERT INTO projects VALUES ('1', '1', 'project name', 'description', NULL, 0, 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO projects VALUES ('2', '1', 'project name', 'description', NULL, 0, 1, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO projects VALUES ('3', '1', 'project name', 'description', NULL, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO resources VALUES ('c8377c44be3db57b5478f9a1eb9c4803', 'route1', '4', '1', '1', 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources VALUES ('c70cb6f0a4d483ec9a04720d3b512211', 'route2', '4', '1', '1', 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources VALUES ('238d7475ace7128fa5395240eb6d8fe9', 'route3', '4', '1', '1', 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources VALUES ('27a2f1042f8cc4948b554d146300cf79', 'route4', '4', '1', '1', 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources VALUES ('2ef802d042a9b0d12d8853f731a14ec8', 'route5', '4', '1', '1', 0, 0, 2, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO user_statuses VALUES (0, 'inactive');
INSERT INTO user_statuses VALUES (1, 'active');

INSERT INTO users VALUES ('1', 'aaa@aaa.com', 0, 'a', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904', NULL, 1, '$2a$10$Fgy0K/pYCrpTjDgzy2VGk.r7SMjJaF1rKa9D0dPjmW1Pej/Ld/WJq');
INSERT INTO users VALUES ('2', 'bbb@aaa.com', 0, 'a', '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904', NULL, 1, '$2a$10$Fgy0K/pYCrpTjDgzy2VGk.r7SMjJaF1rKa9D0dPjmW1Pej/Ld/WJq');

INSERT INTO verbs VALUES (1, 'get');
INSERT INTO verbs VALUES (2, 'post');
INSERT INTO verbs VALUES (3, 'put');
INSERT INTO verbs VALUES (4, 'delete');

ALTER TABLE ONLY access_tokens
    ADD CONSTRAINT access_token_pkey PRIMARY KEY (access_token);

ALTER TABLE ONLY actions
    ADD CONSTRAINT actions_pkey PRIMARY KEY (id);

ALTER TABLE ONLY auth_codes
    ADD CONSTRAINT auth_code_pkey PRIMARY KEY (authorization_code);

ALTER TABLE ONLY clients
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);

ALTER TABLE ONLY entity_types
    ADD CONSTRAINT entity_types_pkey PRIMARY KEY (id);

ALTER TABLE ONLY extra_fields
    ADD CONSTRAINT extra_fields_pkey PRIMARY KEY (id);

ALTER TABLE ONLY extra_fields_values
    ADD CONSTRAINT extra_fields_values_pkey PRIMARY KEY (id);

ALTER TABLE ONLY grant_types
    ADD CONSTRAINT grant_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);

ALTER TABLE ONLY groups_users
    ADD CONSTRAINT groups_users_pkey PRIMARY KEY (group_id, user_id);

ALTER TABLE ONLY labels
    ADD CONSTRAINT labels_pkey PRIMARY KEY (id);

ALTER TABLE ONLY client_grant_types
    ADD CONSTRAINT pk_client_grant_type PRIMARY KEY (client_id, grant_type_id);

ALTER TABLE ONLY plugins
    ADD CONSTRAINT plugins_pkey PRIMARY KEY (id);

ALTER TABLE ONLY plugins_projects
    ADD CONSTRAINT plugins_projects_pkey PRIMARY KEY (id);

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);

ALTER TABLE ONLY resources
    ADD CONSTRAINT resources_pkey PRIMARY KEY (id);

ALTER TABLE ONLY plugins_projects
    ADD CONSTRAINT uq_plugin_project UNIQUE (plugin_id, project_id);

ALTER TABLE ONLY actions
    ADD CONSTRAINT uq_url_verb UNIQUE (url, verb);

ALTER TABLE ONLY user_statuses
    ADD CONSTRAINT user_status_pkey PRIMARY KEY (id);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_login_key UNIQUE (login);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY verbs
    ADD CONSTRAINT verbs_pkey PRIMARY KEY (id);

ALTER TABLE ONLY actions
    ADD CONSTRAINT actions_group_id_fkey FOREIGN KEY (group_id) REFERENCES groups(id);

ALTER TABLE ONLY actions
    ADD CONSTRAINT actions_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY actions
    ADD CONSTRAINT actions_verb_fkey FOREIGN KEY (verb) REFERENCES verbs(id);

ALTER TABLE ONLY extra_fields
    ADD CONSTRAINT extra_fields_entity_type_id_fkey FOREIGN KEY (entity_type_id) REFERENCES entity_types(id);

ALTER TABLE ONLY extra_fields
    ADD CONSTRAINT extra_fields_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES extra_fields(id);

ALTER TABLE ONLY extra_fields
    ADD CONSTRAINT extra_fields_plugin_id_fkey FOREIGN KEY (plugin_id) REFERENCES plugins(id);

ALTER TABLE ONLY extra_fields_values
    ADD CONSTRAINT extra_fields_values_entity_id_fkey FOREIGN KEY (entity_id) REFERENCES users(id);

ALTER TABLE ONLY extra_fields_values
    ADD CONSTRAINT extra_fields_values_extra_field_id_fkey FOREIGN KEY (extra_field_id) REFERENCES extra_fields(id);

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_project_id_fkey FOREIGN KEY (project_id) REFERENCES projects(id);

ALTER TABLE ONLY groups_users
    ADD CONSTRAINT groups_users_group_id_fkey FOREIGN KEY (group_id) REFERENCES groups(id);

ALTER TABLE ONLY groups_users
    ADD CONSTRAINT groups_users_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY labels
    ADD CONSTRAINT labels_entity_type_id_fkey FOREIGN KEY (entity_type_id) REFERENCES entity_types(id);

ALTER TABLE ONLY plugins
    ADD CONSTRAINT plugins_group_id_fkey FOREIGN KEY (group_id) REFERENCES groups(id);

ALTER TABLE ONLY plugins
    ADD CONSTRAINT plugins_project_id_fkey FOREIGN KEY (project_id) REFERENCES projects(id);

ALTER TABLE ONLY plugins_projects
    ADD CONSTRAINT plugins_projects_plugin_id_fkey FOREIGN KEY (plugin_id) REFERENCES plugins(id);

ALTER TABLE ONLY plugins_projects
    ADD CONSTRAINT plugins_projects_project_id_fkey FOREIGN KEY (project_id) REFERENCES projects(id);

ALTER TABLE ONLY plugins
    ADD CONSTRAINT plugins_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES projects(id);

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY resources
    ADD CONSTRAINT resources_entity_type_id_fkey FOREIGN KEY (entity_type_id) REFERENCES entity_types(id);

ALTER TABLE ONLY resources
    ADD CONSTRAINT resources_group_id_fkey FOREIGN KEY (group_id) REFERENCES groups(id);

ALTER TABLE ONLY resources
    ADD CONSTRAINT resources_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_status_fkey FOREIGN KEY (status) REFERENCES user_statuses(id);

# --- !Downs
