--liquibase formatted sql

--changeset shaplov:1

create table car_users (
                         id serial primary key,
                         login varchar(100) unique not null,
                         password varchar(50) not null,
                         created timestamp
);

create table brand (
                     id serial primary key,
                     title varchar(100)
);

create table model (
                     id serial primary key,
                     title varchar(100),
                     brand_id int references brand(id)
);

create table body_type (
                         id serial primary key,
                         title varchar(100)
);

create table engine_type (
                           id serial primary key,
                           title varchar(100)
);

create table drive_type (
                          id serial primary key,
                          title varchar(100)
);

create table trans_type (
                          id serial primary key,
                          title varchar(100)
);

create table items (
                     id serial primary key,
                     title varchar(200),
                     sold boolean,
                     created timestamp,
                     car_users_id int references car_users(id),
                     brand_id int references brand(id),
                     model_id int references model(id),
                     body_type_id int references body_type(id),
                     engine_type_id int references engine_type(id),
                     drive_type_id int references drive_type(id),
                     trans_type_id int references trans_type(id)
);

create table unifying (
                        id serial primary key,
                        model_id int references model(id),
                        body_type_id int references body_type(id),
                        engine_type_id int references engine_type(id),
                        drive_type_id int references drive_type(id),
                        trans_type_id int references trans_type(id)
);

--rollback drop table unifying; drop table items; drop table users; drop table brand; drop table model; drop table body_type; drop table engine_type; drop table drive_type; drop table trans_type;

--changeset shaplov:2
insert into brand (title) values ('BMW');
insert into model (title, brand_id) values ('1 серия', (select id from brand where title = 'BMW'));
insert into body_type(title) values ('кабриолет'), ('купе'), ('хетчбэк');
insert into engine_type(title) values ('Бензин'), ('Дизель');
insert into drive_type(title) values ('Задний'), ('Передний'), ('Полный');
insert into trans_type(title) values ('Автомат'), ('Механика');
insert into unifying(model_id, body_type_id, engine_type_id, drive_type_id, trans_type_id) values

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика')),

((select id from model where title = '1 серия'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат'));

--rollback delete from model; delete from brand; delete from unifying; delete from body_type; delete from engine_type; delete from drive_type; delete from trans_type;

--changeset shaplov:3
alter table car_users add column tel varchar(50);
--rollback alter table car_users drop column tel;

--changeset shaplov:4
alter table items add column picture varchar(200);
--rollback alter table car_users drop column picture;

--changeset shaplov:5

insert into brand (title) values ('Audi'), ('Bentley'),
                                 ('Nissan'), ('ВАЗ (LADA)'), ('Jaguar');

insert into model (title, brand_id) values ('A8', (select id from brand where title = 'Audi')),
                                           ('A5', (select id from brand where title = 'Audi')),
                                           ('A1', (select id from brand where title = 'Audi')),
                                           ('Azure', (select id from brand where title = 'Bentley')),
                                           ('Brooklands', (select id from brand where title = 'Bentley')),
                                           ('Mulsanne', (select id from brand where title = 'Bentley')),
                                           ('Almera', (select id from brand where title = 'Nissan')),
                                           ('Teana', (select id from brand where title = 'Nissan')),
                                           ('GT-R', (select id from brand where title = 'Nissan')),
                                           ('Vesta', (select id from brand where title = 'ВАЗ (LADA)')),
                                           ('Kalina', (select id from brand where title = 'ВАЗ (LADA)')),
                                           ('Granta', (select id from brand where title = 'ВАЗ (LADA)')),
                                           ('Priora', (select id from brand where title = 'ВАЗ (LADA)')),
                                           ('S-Type', (select id from brand where title = 'Jaguar')),
                                           ('F-Type', (select id from brand where title = 'Jaguar')),
                                           ('E-Type', (select id from brand where title = 'Jaguar'));

insert into body_type(title) values ('седан'), ('универсал');
insert into trans_type(title) values ('Робот'), ('Вариатор');

insert into unifying(model_id, body_type_id, engine_type_id, drive_type_id, trans_type_id) values
((select id from model where title = 'A1'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A1'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Вариатор')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Вариатор')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Робот')),
  ((select id from model where title = 'A5'),
  (select id from body_type where title = 'купе'),
  (select id from engine_type where title = 'Дизель'),
  (select id from drive_type where title = 'Передний'),
  (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Вариатор')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Вариатор')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'A5'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'A8'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Azure'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Brooklands'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Mulsanne'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Almera'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Almera'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Teana'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Вариатор')),
((select id from model where title = 'GT-R'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Granta'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Kalina'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Kalina'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Kalina'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Kalina'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Kalina'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Kalina'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Priora'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Priora'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Priora'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Priora'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Priora'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'Priora'),
 (select id from body_type where title = 'хетчбэк'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Vesta'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'Vesta'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Vesta'),
(select id from body_type where title = 'седан'),
(select id from engine_type where title = 'Газ'),
(select id from drive_type where title = 'Передний'),
(select id from trans_type where title = 'Механика')),
((select id from model where title = 'Vesta'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'Vesta'),
 (select id from body_type where title = 'универсал'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Передний'),
 (select id from trans_type where title = 'Робот')),
((select id from model where title = 'E-Type'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'E-Type'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'F-Type'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'F-Type'),
 (select id from body_type where title = 'кабриолет'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'F-Type'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Полный'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'F-Type'),
 (select id from body_type where title = 'купе'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'S-Type'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'S-Type'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Бензин'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика')),
((select id from model where title = 'S-Type'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Автомат')),
((select id from model where title = 'S-Type'),
 (select id from body_type where title = 'седан'),
 (select id from engine_type where title = 'Дизель'),
 (select id from drive_type where title = 'Задний'),
 (select id from trans_type where title = 'Механика'));

--changeset shaplov:6

delete from items;

create table picture_lob (
  id serial primary key,
  img bytea,
  mime_type varchar(100)
)


