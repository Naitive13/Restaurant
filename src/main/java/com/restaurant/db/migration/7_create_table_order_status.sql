create type statusType as ENUM ('CREATED','CONFIRMED','IN_PROGRESS','DONE','DELIVERED');
create table if not exists order_status (
    id serial primary key,
    order_reference varchar(20) references "order"(order_reference),
    order_status statusType,
    creation_date timestamp
);