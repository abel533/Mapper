drop table user if exists;

create table user (
  id integer NOT NULL PRIMARY KEY,
  name varchar(32),
  address varchar(64),
  state integer
);

INSERT INTO user (id, name, address, state) VALUES (1, 'abel533', '河北省/石家庄市', 1);
INSERT INTO user (id, name, address, state) VALUES (2, 'isea533', '河北省/邯郸市', 0);