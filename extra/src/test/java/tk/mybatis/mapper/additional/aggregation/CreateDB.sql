drop table user if exists;

create table user (
  id          integer NOT NULL PRIMARY KEY,
  name varchar(32),
  ro_le VARCHAR(32)
);

INSERT INTO user (id, name, ro_le) VALUES (1, 'Angola', 'Admin');
INSERT INTO user (id, name, ro_le) VALUES (2, 'Afghanistan', 'Admin');
INSERT INTO user (id, name, ro_le) VALUES (3, 'Albania', 'Admin');
INSERT INTO user (id, name, ro_le) VALUES (4, 'Algeria', 'USER');
INSERT INTO user (id, name, ro_le) VALUES (5, 'Andorra', 'USER');
INSERT INTO user (id, name, ro_le) VALUES (6, 'Anguilla', 'USER');