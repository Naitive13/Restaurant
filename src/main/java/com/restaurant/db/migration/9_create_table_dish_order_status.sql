create table if not exists dish_order_status(
    id serial primary key,
    dish_order_id bigint references dish_order(dish_order_id),
    dish_order_status statusType,
    creation_date timestamp,
    unique (dish_order_id, dish_order_status)
);