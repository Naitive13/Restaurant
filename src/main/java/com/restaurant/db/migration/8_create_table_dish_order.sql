create table if not exists dish_order (
    dish_order_id bigint primary key,
    dish_id bigint references dish(dish_id),
    order_reference varchar(20) references "sortOrder"(order_reference),
    quantity int
);