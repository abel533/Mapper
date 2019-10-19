drop table book if exists;

create table book (
  id   integer NOT NULL PRIMARY KEY,
  name varchar(32),
  price integer,
  published date
);

INSERT INTO book VALUES (1, 'Java入门1', '50', '2015-11-11');
INSERT INTO book VALUES (2, 'Java入门2', '50', '2015-11-11');
INSERT INTO book VALUES (3, 'Java中级', '80', '2017-11-11');
INSERT INTO book VALUES (4, 'Java高级', '100', '2019-11-11');
