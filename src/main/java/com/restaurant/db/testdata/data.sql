insert into dish (dish_id,dish_name, dish_price)
        values (1,'hotdog', 15000);

insert into ingredient (ingredient_id, ingredient_name, unit, last_modified)
        values  (1,'sausage','G', '2025-01-01 00:00:00'),
                (2,'oil','L', '2025-01-01 00:00:00'),
                (3,'egg','U', '2025-01-01 00:00:00'),
                (4,'bread','U', '2025-01-01 00:00:00');

insert into ingredient_price (ingredient_id, unit_price, price_date)
        values  (1, 20, '2025-01-01 00:00:00'),
                (2, 10000, '2025-01-01 00:00:00'),
                (3, 1000, '2025-01-01 00:00:00'),
                (4, 1000, '2025-01-01 00:00:00');

insert into dish_ingredient (dish_id, ingredient_id, quantity)
        values  (1,1,100),
                (1,2,0.15),
                (1,3,1),
                (1,4,1);
insert into stock_in (ingredient_id, quantity, last_modified)
        values  (1, 0, '2025-01-01 01:00:00'),
                (2, 0, '2025-01-01 01:00:00'),
                (3, 0, '2025-01-01 01:00:00'),
                (4, 0, '2025-01-01 01:00:00'),
                (3, 100, '2025-02-01 08:00:00'),
                (4, 50, '2025-02-01 08:00:00'),
                (1, 10000, '2025-02-01 08:00:00'),
                (2, 20, '2025-02-01 08:00:00');
--insert into stock_out (ingredient_id, quantity, last_modified)
--     values  (1, 0, '2025-01-01 01:00:00'),
--             (2, 0, '2025-01-01 01:00:00'),
--            (3, 0, '2025-01-01 01:00:00'),
--             (4, 0, '2025-01-01 01:00:00');

INSERT INTO stock_out (ingredient_id, quantity, last_modified)
       VALUES  (1, 0, '2025-01-01 01:00:00'),
               (2, 0, '2025-01-01 01:00:00'),
               (3, 0, '2025-01-01 01:00:00'),
               (4, 0, '2025-01-01 01:00:00'),
               (3, 10, '2025-02-02 10:00:00'),
               (3, 10, '2025-02-03 15:00:00'),
               (4, 20, '2025-02-05 16:00:00');

--with so as (select ingredient_id, sum(quantity) as available from stock_out group by ingredient_id),
--si as (select ingredient_id, sum(quantity) as available from stock_in group by ingredient_id)
--select si.ingredient_id, (si.available - so.available) as quantity from si
--join so on si.ingredient_id=so.ingredient_id;
