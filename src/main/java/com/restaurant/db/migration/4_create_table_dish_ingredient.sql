CREATE TABLE IF NOT EXISTS dish_ingredient (
    dish_id bigint REFERENCES dish(dish_id),
    ingredient_id bigint REFERENCES ingredient(ingredient_id),
    quantity NUMERIC(10,2) not null,
    UNIQUE (dish_id, ingredient_id)
);
