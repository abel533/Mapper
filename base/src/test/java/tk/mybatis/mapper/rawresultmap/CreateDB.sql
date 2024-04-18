drop table user if exists;

create table user
(
    id    integer NOT NULL PRIMARY KEY,
    name varchar(32),
    user_name  varchar(32),
    email  varchar(32),
    age__int__aa  integer,
    create_time datetime
);

INSERT INTO user (id, name, user_name, email, age__int__aa, create_time)
VALUES (1, 'trifolium1', 'wang1', 'email1', 23, now());
INSERT INTO user (id, name, user_name, email, age__int__aa, create_time)
VALUES (2, 'trifolium2', 'wang2', 'email2', 32, now());