drop table country if exists;

create table country
(
    id          integer              NOT NULL PRIMARY KEY,
    countryname varchar(32),
    countrycode VARCHAR(2) DEFAULT 'HH',
    version     INTEGER    DEFAULT 1 NOT NULL
);

drop table country2 if exists;
create table country2
(
    id          integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    countryname varchar(32),
    countrycode varchar(2) DEFAULT 'HH'
);

drop table country_t if exists;
create table country_t
(
    id          integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    countryname varchar(32)
);

drop table country_jdbc if exists;
create table country_jdbc
(
    id          integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    countryname varchar(128),
    countrycode varchar(2)
);

drop table country_i if exists;
create table country_i
(
    id          integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    countryname varchar(128),
    countrycode varchar(2) DEFAULT 'HH'
);

--用户信息表
drop table user_info if exists;
create table user_info
(
    id       integer GENERATED BY DEFAULT AS IDENTITY (START WITH 6) NOT NULL PRIMARY KEY,
    username varchar(32)                                             NOT NULL,
    password varchar(32) DEFAULT '12345678',
    usertype varchar(2),
    enabled  integer,
    realname varchar(50),
    qq       varchar(12),
    email    varchar(100),
    address  varchar(200),
    tel      varchar(30)
);

--用户信息表
drop table user_info_map if exists;
create table user_info_map
(
    id        integer GENERATED BY DEFAULT AS IDENTITY (START WITH 6) NOT NULL PRIMARY KEY,
    user_name varchar(32)                                             NOT NULL,
    password  varchar(32) DEFAULT '12345678',
    user_type varchar(2),
    real_name varchar(50)
);

insert into user_info (id, username, password, usertype)
values (1, 'test1', '12345678', '1');
insert into user_info (id, username, password, usertype)
values (2, 'test2', 'aaaa', '2');
insert into user_info (id, username, password, usertype)
values (3, 'test3', 'bbbb', '1');
insert into user_info (id, username, password, usertype)
values (4, 'test4', 'cccc', '2');
insert into user_info (id, username, password, usertype)
values (5, 'test5', 'dddd', '1');

insert into user_info_map (id, user_name, password, user_type)
values (1, 'test1', '12345678', '1');
insert into user_info_map (id, user_name, password, user_type)
values (2, 'test2', 'aaaa', '2');
insert into user_info_map (id, user_name, password, user_type)
values (3, 'test3', 'bbbb', '1');
insert into user_info_map (id, user_name, password, user_type)
values (4, 'test4', 'cccc', '2');
insert into user_info_map (id, user_name, password, user_type)
values (5, 'test5', 'dddd', '1');


--用户登录表,logid和username联合主键
drop table user_login if exists;
create table user_login
(
    logid     integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL,
    username  varchar(32)                                             NOT NULL,
    logindate DATETIME,
    loginip   varchar(16),
    PRIMARY KEY (logid, username)
);

insert into user_login (logid, username, logindate, loginip)
values (1, 'test1', '2014-10-11 12:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (2, 'test1', '2014-10-21 12:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (3, 'test1', '2014-10-21 14:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (4, 'test1', '2014-11-21 11:20:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (5, 'test1', '2014-11-21 13:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (6, 'test2', '2014-11-21 12:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (7, 'test2', '2014-11-21 12:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (8, 'test3', '2014-11-21 12:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (9, 'test4', '2014-11-21 12:00:00', '192.168.1.123');
insert into user_login (logid, username, logindate, loginip)
values (10, 'test5', '2014-11-21 12:00:00', '192.168.1.123');


INSERT INTO country (id, countryname, countrycode, version)
VALUES (1, 'Angola', 'AO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (2, 'Afghanistan', 'AF', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (3, 'Albania', 'AL', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (4, 'Algeria', 'DZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (5, 'Andorra', 'AD', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (6, 'Anguilla', 'AI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (7, 'Antigua and Barbuda', 'AG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (8, 'Argentina', 'AR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (9, 'Armenia', 'AM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (10, 'Australia', 'AU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (11, 'Austria', 'AT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (12, 'Azerbaijan', 'AZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (13, 'Bahamas', 'BS', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (14, 'Bahrain', 'BH', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (15, 'Bangladesh', 'BD', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (16, 'Barbados', 'BB', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (17, 'Belarus', 'BY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (18, 'Belgium', 'BE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (19, 'Belize', 'BZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (20, 'Benin', 'BJ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (21, 'Bermuda Is.', 'BM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (22, 'Bolivia', 'BO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (23, 'Botswana', 'BW', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (24, 'Brazil', 'BR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (25, 'Brunei', 'BN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (26, 'Bulgaria', 'BG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (27, 'Burkina-faso', 'BF', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (28, 'Burma', 'MM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (29, 'Burundi', 'BI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (30, 'Cameroon', 'CM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (31, 'Canada', 'CA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (32, 'Central African Republic', 'CF', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (33, 'Chad', 'TD', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (34, 'Chile', 'CL', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (35, 'China', 'CN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (36, 'Colombia', 'CO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (37, 'Congo', 'CG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (38, 'Cook Is.', 'CK', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (39, 'Costa Rica', 'CR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (40, 'Cuba', 'CU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (41, 'Cyprus', 'CY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (42, 'Czech Republic', 'CZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (43, 'Denmark', 'DK', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (44, 'Djibouti', 'DJ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (45, 'Dominica Rep.', 'DO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (46, 'Ecuador', 'EC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (47, 'Egypt', 'EG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (48, 'EI Salvador', 'SV', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (49, 'Estonia', 'EE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (50, 'Ethiopia', 'ET', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (51, 'Fiji', 'FJ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (52, 'Finland', 'FI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (53, 'France', 'FR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (54, 'French Guiana', 'GF', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (55, 'Gabon', 'GA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (56, 'Gambia', 'GM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (57, 'Georgia', 'GE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (58, 'Germany', 'DE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (59, 'Ghana', 'GH', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (60, 'Gibraltar', 'GI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (61, 'Greece', 'GR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (62, 'Grenada', 'GD', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (63, 'Guam', 'GU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (64, 'Guatemala', 'GT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (65, 'Guinea', 'GN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (66, 'Guyana', 'GY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (67, 'Haiti', 'HT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (68, 'Honduras', 'HN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (69, 'Hongkong', 'HK', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (70, 'Hungary', 'HU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (71, 'Iceland', 'IS', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (72, 'India', 'IN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (73, 'Indonesia', 'ID', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (74, 'Iran', 'IR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (75, 'Iraq', 'IQ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (76, 'Ireland', 'IE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (77, 'Israel', 'IL', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (78, 'Italy', 'IT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (79, 'Jamaica', 'JM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (80, 'Japan', 'JP', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (81, 'Jordan', 'JO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (82, 'Kampuchea (Cambodia )', 'KH', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (83, 'Kazakstan', 'KZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (84, 'Kenya', 'KE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (85, 'Korea', 'KR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (86, 'Kuwait', 'KW', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (87, 'Kyrgyzstan', 'KG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (88, 'Laos', 'LA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (89, 'Latvia', 'LV', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (90, 'Lebanon', 'LB', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (91, 'Lesotho', 'LS', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (92, 'Liberia', 'LR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (93, 'Libya', 'LY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (94, 'Liechtenstein', 'LI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (95, 'Lithuania', 'LT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (96, 'Luxembourg', 'LU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (97, 'Macao', 'MO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (98, 'Madagascar', 'MG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (99, 'Malawi', 'MW', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (100, 'Malaysia', 'MY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (101, 'Maldives', 'MV', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (102, 'Mali', 'ML', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (103, 'Malta', 'MT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (104, 'Mauritius', 'MU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (105, 'Mexico', 'MX', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (106, 'Moldova, Republic of', 'MD', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (107, 'Monaco', 'MC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (108, 'Mongolia', 'MN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (109, 'Montserrat Is', 'MS', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (110, 'Morocco', 'MA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (111, 'Mozambique', 'MZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (112, 'Namibia', 'NA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (113, 'Nauru', 'NR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (114, 'Nepal', 'NP', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (115, 'Netherlands', 'NL', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (116, 'New Zealand', 'NZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (117, 'Nicaragua', 'NI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (118, 'Niger', 'NE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (119, 'Nigeria', 'NG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (120, 'North Korea', 'KP', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (121, 'Norway', 'NO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (122, 'Oman', 'OM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (123, 'Pakistan', 'PK', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (124, 'Panama', 'PA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (125, 'Papua New Cuinea', 'PG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (126, 'Paraguay', 'PY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (127, 'Peru', 'PE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (128, 'Philippines', 'PH', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (129, 'Poland', 'PL', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (130, 'French Polynesia', 'PF', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (131, 'Portugal', 'PT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (132, 'Puerto Rico', 'PR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (133, 'Qatar', 'QA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (134, 'Romania', 'RO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (135, 'Russia', 'RU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (136, 'Saint Lueia', 'LC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (137, 'Saint Vincent', 'VC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (138, 'San Marino', 'SM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (139, 'Sao Tome and Principe', 'ST', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (140, 'Saudi Arabia', 'SA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (141, 'Senegal', 'SN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (142, 'Seychelles', 'SC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (143, 'Sierra Leone', 'SL', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (144, 'Singapore', 'SG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (145, 'Slovakia', 'SK', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (146, 'Slovenia', 'SI', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (147, 'Solomon Is', 'SB', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (148, 'Somali', 'SO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (149, 'South Africa', 'ZA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (150, 'Spain', 'ES', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (151, 'Sri Lanka', 'LK', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (152, 'St.Lucia', 'LC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (153, 'St.Vincent', 'VC', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (154, 'Sudan', 'SD', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (155, 'Suriname', 'SR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (156, 'Swaziland', 'SZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (157, 'Sweden', 'SE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (158, 'Switzerland', 'CH', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (159, 'Syria', 'SY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (160, 'Taiwan', 'TW', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (161, 'Tajikstan', 'TJ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (162, 'Tanzania', 'TZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (163, 'Thailand', 'TH', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (164, 'Togo', 'TG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (165, 'Tonga', 'TO', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (166, 'Trinidad and Tobago', 'TT', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (167, 'Tunisia', 'TN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (168, 'Turkey', 'TR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (169, 'Turkmenistan', 'TM', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (170, 'Uganda', 'UG', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (171, 'Ukraine', 'UA', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (172, 'United Arab Emirates', 'AE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (173, 'United Kiongdom', 'GB', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (174, 'United States of America', 'US', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (175, 'Uruguay', 'UY', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (176, 'Uzbekistan', 'UZ', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (177, 'Venezuela', 'VE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (178, 'Vietnam', 'VN', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (179, 'Yemen', 'YE', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (180, 'Yugoslavia', 'YU', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (181, 'Zimbabwe', 'ZW', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (182, 'Zaire', 'ZR', 1);
INSERT INTO country (id, countryname, countrycode, version)
VALUES (183, 'Zambia', 'ZM', 1);



insert into country_t (id, countryname)
values (174, 'United States of America');


--用户信息表
drop table tb_user if exists;
create table tb_user
(
    id       integer GENERATED BY DEFAULT AS IDENTITY (START WITH 6) NOT NULL PRIMARY KEY,
    username varchar(32)                                             NOT NULL,
    password varchar(32) DEFAULT '12345678',
    is_valid integer
);

insert into tb_user (id, username, password, is_valid)
values (1, 'test1', '12345678', 1);
insert into tb_user (id, username, password, is_valid)
values (2, 'test2', 'aaaa', 1);
insert into tb_user (id, username, password, is_valid)
values (3, 'test3', 'bbbb', 1);
insert into tb_user (id, username, password, is_valid)
values (4, 'test4', 'cccc', 1);
insert into tb_user (id, username, password, is_valid)
values (5, 'test5', 'dddd', 0);
insert into tb_user (id, username, password, is_valid)
values (6, 'test6', 'eeee', 0);
insert into tb_user (id, username, password, is_valid)
values (7, 'test7', 'ffff', 0);
insert into tb_user (id, username, password, is_valid)
values (8, 'test', 'gggg', 1);
insert into tb_user (id, username, password, is_valid)
values (9, 'test', 'gggg', 0);