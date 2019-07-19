--liquibase formatted sql

--changeset shaplov:1
create table users (
  id serial primary key,
  name varchar(2000),
  expire_date timestamp
);
--rollback drop table users;

--changeset shaplov:2
create table item (
  id serial primary key,
  description text not null,
  created timestamp,
  done boolean default false
);
--rollback drop table item;