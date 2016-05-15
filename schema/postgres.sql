--
-- create tables
--
CREATE TABLE blog (
    id integer NOT NULL,
    userid integer NOT NULL,
    created timestamp with time zone,
    story varchar(256),
    CONSTRAINT blog_pkey PRIMARY KEY (id)
);

CREATE TABLE groups (
    groupid integer NOT NULL,
    role varchar(30) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (groupid)
);

CREATE TABLE users (
    userid integer NOT NULL,
    groupid integer NOT NULL,
    username varchar(60) NOT NULL,
    password varchar(64) NOT NULL,
    last varchar(30) NOT NULL,
    first varchar(30) NOT NULL,
    middle varchar(30),
    avatar varchar(30),
    CONSTRAINT users_pkey PRIMARY KEY (userid)
);

CREATE TABLE sessions (
    sessid varchar(32) NOT NULL,
    inet bigint NOT NULL,
    userid integer,
    created timestamp with time zone,
    accessed timestamp with time zone,
    status char(1),
    CONSTRAINT sessid_pkey PRIMARY KEY (sessid)
);

CREATE TABLE settings (
    name VARCHAR(20),
    value VARCHAR(512),
    CONSTRAINT settings_pkey PRIMARY KEY (name)
);

--
-- define contraints (foreign keys, etc.)
--
ALTER TABLE blog
    ADD CONSTRAINT blog_fkey FOREIGN KEY (userid) REFERENCES users (userid)
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE users
     ADD CONSTRAINT users_fkey FOREIGN KEY (groupid) REFERENCES groups (groupid)
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE sessions
    ADD CONSTRAINT sessions_fkey FOREIGN KEY (userid) REFERENCES users (userid)
    ON UPDATE CASCADE ON DELETE CASCADE;

--
-- seed data
--
INSERT INTO groups VALUES (10, 'Wheel');
INSERT INTO groups VALUES (20, 'Admin');
INSERT INTO groups VALUES (100, 'Provider');
INSERT INTO groups VALUES (200, 'Customer');
INSERT INTO groups VALUES (500, 'Visitor');

INSERT INTO users (userid,groupid, username,password, last,first, avatar)
VALUES (10,10, 'dba','!', 'oracle','delphi', 'system');

INSERT INTO users (userid,groupid,username,password,last,first, avatar)
 VALUES (20,20, 'avatar','3e1ee0f1cf1c6c1013e9618ec28b5c127b9be561', 'Sully','Jake', 'system');

INSERT INTO users (userid,groupid,username,password,last,first, avatar)
 VALUES (500,500, 'visitor','x', 'Welcome','Guest', 'system');

INSERT INTO blog
 VALUES (1, 20, '2015-09-03 06:06:06-07', 'Semper idem');

INSERT INTO blog
 VALUES (2, 20, '2015-09-03 06:06:06-07', 'Vir sapit qui pauca loquitur');

INSERT INTO settings VALUES ('landing', 'mainpage');
INSERT INTO settings VALUES ('salt', 'Sub rosa');
