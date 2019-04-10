drop table user if exists;

create table user (
  id          integer NOT NULL PRIMARY KEY,
  name varchar(32),
  role VARCHAR(32)
);

INSERT INTO user (id, name, role) VALUES (1, 'Angola', 'Admin');
INSERT INTO user (id, name, role) VALUES (2, 'Afghanistan', 'Admin');
INSERT INTO user (id, name, role) VALUES (3, 'Albania', 'Admin');
INSERT INTO user (id, name, role) VALUES (4, 'Algeria', 'USER');
INSERT INTO user (id, name, role) VALUES (5, 'Andorra', 'USER');
INSERT INTO user (id, name, role) VALUES (6, 'Anguilla', 'USER');