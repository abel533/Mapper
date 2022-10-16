drop table course if exists;

create table course
(
    id         integer NOT NULL PRIMARY KEY,
    name       varchar(32),
    price      integer,
    published  date,
    is_deleted integer
);

INSERT INTO course
VALUES (1, 'JavaStarter1', '50', '2015-11-11', '0');
INSERT INTO course
VALUES (2, 'JavaStarter2', '50', '2015-11-11', '0');
INSERT INTO course
VALUES (3, 'Java3', '80', '2017-11-11', '0');
INSERT INTO course
VALUES (4, 'Java4', '100', '2019-11-11', '0');
