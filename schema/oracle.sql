CREATE TABLE blog (
    id INTEGER NOT NULL,
    userid INTEGER NOT NULL,
    created TIMESTAMP(0),
    story VARCHAR(256),
    CONSTRAINT glob_pkey PRIMARY KEY (id)
);

CREATE TABLE groups (
    groupid integer NOT NULL,
    role varchar(30) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (groupid)
);

CREATE TABLE users (
    userid INTEGER NOT NULL,
    groupid INTEGER NOT NULL,
    username varchar(60) NOT NULL,
    password varchar(64) NOT NULL,
    last VARCHAR(30) NOT NULL,
    first VARCHAR(30) NOT NULL,
    middle VARCHAR(30),
    avatar VARCHAR(30),
    CONSTRAINT users_pkey PRIMARY KEY (userid)
);

CREATE TABLE sessions (
    sessid VARCHAR(32) NOT NULL,
    inet INTEGER NOT NULL,
    userid INTEGER,
    created TIMESTAMP(0),
    accessed TIMESTAMP(0),
    status CHAR(1),
    CONSTRAINT sessid_pkey PRIMARY KEY (sessid)
);

CREATE TABLE settings (
    name VARCHAR(20),
    value VARCHAR(512),
    CONSTRAINT settings_pkey PRIMARY KEY (name)
);

ALTER TABLE blog ADD CONSTRAINT glob_fkey
    FOREIGN KEY (userid) REFERENCES users (userid)
    NOT DEFERRABLE;

ALTER TABLE users
    ADD CONSTRAINT users_fkey FOREIGN KEY (groupid) REFERENCES groups (groupid)
    NOT DEFERRABLE;

ALTER TABLE sessions
    ADD CONSTRAINT sessions_fkey FOREIGN KEY (userid) REFERENCES users (userid)
    NOT DEFERRABLE;

INSERT INTO groups VALUES (10, 'Wheel');
INSERT INTO groups VALUES (20, 'Admin');
INSERT INTO groups VALUES (100, 'Provider');
INSERT INTO groups VALUES (200, 'Customer');
INSERT INTO groups VALUES (500, 'Visitor');

INSERT INTO users (userid,groupid, username,password, last,first, avatar)
VALUES (10,10, 'dba','!', 'oracle','delphi', 'system');

INSERT INTO users (userid,groupid, username,password, last,first, avatar)
VALUES (20,20, 'avatar','3e1ee0f1cf1c6c1013e9618ec28b5c127b9be561', 'Sully','Jake', 'system');

INSERT INTO users (userid,groupid,username,password,last,first, avatar)
 VALUES (500,500, 'visitor','x', 'Welcome','Guest', 'system');

INSERT INTO blog
 VALUES (1, 20, '09-SEP-2015 06.06.06 AM', 'Semper idem');

INSERT INTO settings VALUES ('landing', 'mainpage');
INSERT INTO settings VALUES ('salt', 'Sub rosa');

