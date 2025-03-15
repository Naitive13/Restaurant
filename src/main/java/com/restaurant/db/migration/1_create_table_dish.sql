CREATE TABLE IF NOT EXISTS dish (
    dish_id  bigint PRIMARY KEY,
    dish_name varchar (100) not null unique,
    dish_price bigint not null
);
