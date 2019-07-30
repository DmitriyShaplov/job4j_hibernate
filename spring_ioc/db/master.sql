--liquibase formatted sql

--changeset shaplov:1

create table demo_users (
  id serial primary key,
  name varchar(50)
);