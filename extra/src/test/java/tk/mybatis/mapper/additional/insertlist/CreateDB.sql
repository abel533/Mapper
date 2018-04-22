drop table user if exists;

create table user (
  id   varchar(64) NOT NULL PRIMARY KEY,
  name varchar(32),
  role VARCHAR(32)
);