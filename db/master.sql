--liquibase formatted sql

--changeset shaplov:1
create table users (
  id serial primary key,
  name varchar(2000),
  expire_date timestamp
);
--rollback drop table users;