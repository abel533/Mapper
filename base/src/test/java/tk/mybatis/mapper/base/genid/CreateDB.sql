drop table country if exists;
drop table user if exists;

create table country (
  id          bigint NOT NULL PRIMARY KEY,
  countryname varchar(32),
  countrycode VARCHAR(2)
);

create table user (
  id   varchar(64) NOT NULL PRIMARY KEY,
  name varchar(32),
  code VARCHAR(2)
);


