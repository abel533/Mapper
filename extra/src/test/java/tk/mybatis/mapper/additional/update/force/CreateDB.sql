drop table country_int if exists;


create table country_int (
  id          integer NOT NULL PRIMARY KEY,
  countryname varchar(32),
  countrycode integer
);

INSERT INTO country_int (id, countryname, countrycode) VALUES (174, 'United States of America', 100);
