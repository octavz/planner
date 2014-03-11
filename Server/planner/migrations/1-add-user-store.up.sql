create table user_status(
        id int primary key,
        description varchar(100)
);

create table users (
        id varchar(40) primary key,
        login varchar(30) unique,
        openid_type int not null default 0,
        openid_token varchar(100),
        created timestamp default now(),
        updated timestamp,
        last_login timestamp,
        status int not null default 0 references user_status(id),
        password varchar(100));

create table oauth_clients (
        id varchar(40) primary key,
        secret varchar(100),
        name varchar(255),
        url varchar(255),
        created timestamp default now(),
        updated timestamp);

create table oauth_tokens (
        id varchar(200) primary key,
        user_id varchar(40) references users(id),
        client_id varchar(40) references oauth_clients(id),
        expires timestamp,
        created timestamp default now(),
        updated timestamp,
        scope varchar(1000),
        object varchar(1000),
        constraint uq_user_client unique(user_id,client_id)
);

create table oauth_codes (
        id varchar(200) primary key,
        client_id varchar(40) references oauth_clients(id),
        user_id varchar(40) references users(id),
        redirect_uri varchar(255),
        expires timestamp,
        created timestamp default now(),
        updated timestamp,
        scope varchar(1000),
        object varchar(1000));

create table groups (
        id varchar(40) primary key,
        name varchar(200) not null
);

create table groups_users (
        id varchar(40) primary key,
        group_id varchar(40) not null references groups(id),
        user_id varchar(40) not null references users(id),
        constraint uq_user_group unique(user_id,group_id)
);

create table plugins (
        id varchar(40) primary key,
        user_id varchar(40) not null references users(id),
        group_id varchar(40) not null references groups(id),
        description text,
        perm char(9) not null default 'rw-rw-rw-'
);

create table projects (
        id varchar(40) primary key,
        user_id varchar(40) not null references users(id),
        group_id varchar(40) not null references groups(id),
        name varchar(255) not null,
        description text, 
        parent_id varchar(40) not null references projects(id),
        status smallint not null default 0,
        perm char(9) not null default 'rw-rw-rw-'
);

create table verbs (
        id int primary key,
        description varchar(100)
);

create table actions (
        id varchar(40) primary key,
        user_id varchar(40) not null references users(id),
        group_id varchar(40) not null references groups(id),
        description varchar(100),
        url varchar(2000) not null,
        verb smallint not null references verbs(id),
        perm char(9) not null default 'rw-rw-rw-',
        secured smallint not null,
        constraint uq_url_verb unique(url,verb)
);

create table plugins_projects (
        id varchar(40) primary key,
        plugin_id varchar(40) not null references plugins(id),
        project_id varchar(40) not null references projects(id),
        enabled smallint not null default 1,
        constraint uq_plugin_project unique(plugin_id,project_id)
);

create table entity_types (
        id varchar(40) primary key,
        description varchar(100)
);

create table extra_fields (
        id varchar(40) primary key,
        name varchar(200) not null,
        validation varchar(1000),
        entity_type_id varchar(40) not null references entity_types(id),
        parent_id varchar(40) references extra_fields(id),
        plugin_id varchar(40) not null references plugins(id)
);

create table extra_fields_values (
        id varchar(40) primary key,
        extra_field_id varchar(40) not null references extra_fields(id),
        entity_id varchar(40) not null references users(id),
        value varchar(1000) not null 
);

create table labels (
        id varchar(40) primary key,
        lang varchar(10) not null,
        entity_id varchar(40) not null,
        entity_type_id varchar(40) not null references entity_types(id),
        label1 varchar(1000),
        label2 varchar(1000),
        label3 varchar(1000)
);

create table resources (
        id varchar(40) primary key,
        content varchar(1000),
        entity_type_id varchar(40) not null references entity_types(id),
        user_id varchar(40) not null references users(id),
        group_id varchar(40) not null references groups(id),
        perm char(9) not null default 'rw-rw-rw-'
);

insert into user_status(id,description) values(0,'inactive');
insert into user_status(id,description) values(1,'active');

insert into oauth_clients(id,secret,name) 
  values('1', 'secret','main web app');

insert into users(id,login,openid_token,openid_type,password,status) 
  values('1','a','a',0,
        '$2a$10$OQxEYJuPRvFcrY5oar7OB.RlEWK22rZHyeqqg/r3eO/rtum0vL5GC',1);

insert into groups(id,name) values ('1','admin');

insert into entity_types(id,description) values('1','user');
insert into entity_types(id,description) values('2','project');
insert into entity_types(id,description) values('3','actions');

insert into verbs(id,description) values(1,'get');
insert into verbs(id,description) values(2,'post');
insert into verbs(id,description) values(3,'put');
insert into verbs(id,description) values(4,'delete');

insert into actions(id,url,verb,user_id,group_id,secured) 
  values ('1','/login',1,'1','1',0);
insert into actions(id,url,verb,user_id,group_id,secured) 
  values ('2','/login',2,'1','1',0);
insert into actions(id,url,verb,user_id,group_id,secured) 
  values ('3','/user',1,'1','1',1);
insert into actions(id,url,verb,user_id,group_id,secured) 
  values ('4','/user',3,'1','1',1);
insert into actions(id,url,verb,user_id,group_id,secured) 
  values ('5','/logout',1,'1','1',1);
insert into actions(id,url,verb,user_id,group_id,secured) 
  values ('6','/register',1,'1','1',0);

