create type status as ENUM ('CREATED','CONFIRMED','IN_PROGRESS','DONE','DELIVERED');
create table if not exists order_status (
    id bigint primary key,
    order_reference varchar(20) references "sortOrder"(order_reference),
    order_status status,
    creation_date timestamp
);