drop table book if exists;

create table book
(
    id        integer NOT NULL PRIMARY KEY,
    name      varchar(32),
    price     integer,
    published date
);

INSERT INTO book
VALUES (1, 'JavaStarter1', '50', '2015-11-11');
INSERT INTO book
VALUES (2, 'JavaStarter2', '50', '2015-11-11');
INSERT INTO book
VALUES (3, 'Java3', '80', '2017-11-11');
INSERT INTO book
VALUES (4, 'Java4', '100', '2019-11-11');
