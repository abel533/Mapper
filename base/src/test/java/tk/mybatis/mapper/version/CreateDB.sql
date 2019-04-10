drop table user_timestamp if exists;

CREATE TABLE user_timestamp (
id int NOT NULL,
join_date TIMESTAMP DEFAULT NULL,
PRIMARY KEY (id)
);

insert into user_timestamp values(999, TIMESTAMP '2019-01-01 01:01:11');

drop table user_int if exists;

CREATE TABLE user_int (
id int NOT NULL,
age int DEFAULT NULL,
PRIMARY KEY (id)
);

insert into user_int values(999, 30);