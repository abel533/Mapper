--该脚本需手动导入本地Oracle库
create table demo_country (
  country_id      varchar2(50) constraint pk_demo_country__id primary key,
  country_name    varchar(255) not null,
  country_code    varchar(255) not null
);

create sequence seq_demo_country
  minvalue 1
  maxvalue 9999999999
  start with 200
  increment by 1;

INSERT INTO demo_country(country_id, country_name, country_code) VALUES (1,'Angola','AO');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (2,'Afghanistan','AF');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (3,'Albania','AL');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (4,'Algeria','DZ');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (5,'Andorra','AD');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (6,'Anguilla','AI');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (7,'Antigua and Barbuda','AG');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (8,'Argentina','AR');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (9,'Armenia','AM');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (10,'Australia','AU');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (11,'Austria','AT');
INSERT INTO demo_country(country_id, country_name, country_code) VALUES (12,'Azerbaijan','AZ');
