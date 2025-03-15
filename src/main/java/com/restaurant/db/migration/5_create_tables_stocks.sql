
CREATE TABLE IF NOT EXISTS stock_in (
    id bigint primary key,
    ingredient_id bigint REFERENCES ingredient(ingredient_id),
    quantity NUMERIC(10,2) not null,
    last_modified timestamp not null
);

CREATE TABLE IF NOT EXISTS stock_out (
    id bigint primary key,
    ingredient_id bigint REFERENCES ingredient(ingredient_id),
    quantity NUMERIC(10,2) not null,
    last_modified timestamp not null
);
