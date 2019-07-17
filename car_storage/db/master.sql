--liquibase formatted sql

--changeset shaplov:2
create table car_body (
                        id serial primary key,
                        name varchar(200)
);

create table engine (
                      id serial primary key,
                      name varchar(200)
);

create table transmission (
                            id serial primary key,
                            name varchar(200)
);

create table car (
                   id serial primary key,
                   name varchar(200),
                   car_body_id int references car_body(id),
                   engine_id int references engine(id),
                   transmission_id int references transmission(id)
);
--rollback drop table car; drop table car_body; drop table engine; drop table transmission;
