CREATE DATABASE restaurant;
\c restaurant
CREATE USER db_user WITH PASSWORD '12345678';
GRANT ALL PRIVILEGES ON database LIBRARY to db_user;
GRANT ALL PRIVILEGES ON all tables in SCHEMA PUBLIC to db_user;
GRANT ALL ON SCHEMA PUBLIC to db_user;

\c restaurant db_user;

CREATE TYPE unit_type AS ENUM ('G','L','U');

CREATE TABLE IF NOT EXISTS dish (
    dish_id  bigint PRIMARY KEY,
    dish_name varchar (100) not null unique,
    dish_price bigint not null
);

CREATE TABLE IF NOT EXISTS ingredient (
    ingredient_id bigint PRIMARY KEY,
    ingredient_name varchar (50) not null unique,
    unit unit_type not null,
    last_modified timestamp not null
);

CREATE TABLE IF NOT EXISTS ingredient_price (
    ingredient_id bigint REFERENCES ingredient(ingredient_id),
    unit_price bigint not null,
    price_date timestamp not null,
    UNIQUE (ingredient_id, price_date)
);

CREATE TABLE IF NOT EXISTS dish_ingredient (
    dish_id bigint REFERENCES dish(dish_id),
    ingredient_id bigint REFERENCES ingredient(ingredient_id),
    quantity NUMERIC(10,2) not null,
    UNIQUE (dish_id, ingredient_id)
);

\c titan titan;
--drop owned by db_user;
--drop database restaurant;
--drop role db_user;