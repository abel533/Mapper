drop table user if exists;

create table user (
  id integer NOT NULL PRIMARY KEY,
  name varchar(32),
  lock integer,
  state integer
);

INSERT INTO user (id, name, lock, state) VALUES (1, 'abel533', 2, 1);
INSERT INTO user (id, name, lock, state) VALUES (2, 'isea533', 1, 2);