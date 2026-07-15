drop table user949 if exists;
drop table user_detail949 if exists;

create table user949
(
    id   integer NOT NULL PRIMARY KEY,
    name varchar(32)
);

create table user_detail949
(
    id      integer NOT NULL PRIMARY KEY,
    user_id integer,
    email   varchar(64)
);

INSERT INTO user949 (id, name) VALUES (1, 'user1');
INSERT INTO user949 (id, name) VALUES (2, 'user2');

INSERT INTO user_detail949 (id, user_id, email) VALUES (1, 1, 'user1@example.com');
INSERT INTO user_detail949 (id, user_id, email) VALUES (2, 2, 'user2@example.com');
