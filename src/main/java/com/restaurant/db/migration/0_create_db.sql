create DATABASE restaurant;
\c restaurant
create user db_user with PASSWORD '12345678';
grant all privileges on database restaurant to db_user;
grant all privileges on all tables in SCHEMA public to db_user;
grant all on SCHEMA public to db_user;

\c restaurant db_user
12345678

create type unit_type as ENUM ('G','L','U');






