drop table user if exists;

create table user
(
    id   integer NOT NULL PRIMARY KEY,
    name varchar(32),
    role VARCHAR(32)
);

INSERT INTO user (id, name, role)
VALUES (1, 'Angola', 'Admin');
INSERT INTO user (id, name, role)
VALUES (2, 'Afghanistan', 'Admin');
INSERT INTO user (id, name, role)
VALUES (3, 'Albania', 'Admin');
INSERT INTO user (id, name, role)
VALUES (4, 'Algeria', 'USER');
INSERT INTO user (id, name, role)
VALUES (5, 'Andorra', 'USER');
INSERT INTO user (id, name, role)
VALUES (6, 'Anguilla', 'USER');

drop table user_hump if exists;

create table user_hump (
  id integer NOT NULL PRIMARY KEY,
  user_name varchar(32),
  user_role VARCHAR(32)
);

INSERT INTO user_hump (id, user_name, user_role) VALUES (1, 'Angola', 'Admin');
INSERT INTO user_hump (id, user_name, user_role) VALUES (2, 'Afghanistan', 'Admin');
INSERT INTO user_hump (id, user_name, user_role) VALUES (3, 'Albania', 'Admin');
INSERT INTO user_hump (id, user_name, user_role) VALUES (4, 'Algeria', 'USER');
INSERT INTO user_hump (id, user_name, user_role) VALUES (5, 'Andorra', 'USER');
INSERT INTO user_hump (id, user_name, user_role) VALUES (6, 'Anguilla', 'USER');

