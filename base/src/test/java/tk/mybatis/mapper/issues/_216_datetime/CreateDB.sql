drop table test_timestamp if exists;

create table test_timestamp (
  id          integer NOT NULL PRIMARY KEY,
  test_date DATE,
  test_time TIME,
  test_datetime DATETIME -- 和 TIMESTAMP 相同
);

INSERT INTO test_timestamp (id, test_date, test_time, test_datetime) VALUES (1, DATE '2018-01-01', TIME '12:11:00',TIMESTAMP  '2018-01-01 12:00:00');
INSERT INTO test_timestamp (id, test_date, test_time, test_datetime) VALUES (2, DATE '2018-11-11', TIME '01:59:11',TIMESTAMP  '2018-02-12 17:58:12');