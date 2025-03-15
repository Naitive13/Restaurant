CREATE TABLE IF NOT EXISTS ingredient (
    ingredient_id bigint PRIMARY KEY,
    ingredient_name varchar (50) not null unique,
    unit unit_type not null,
    last_modified timestamp not null
);
