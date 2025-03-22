insert into stock_in (id,ingredient_id, quantity, last_modified)
        values  (1,1, 0, '2025-01-01 01:00:00'),
                (2,2, 0, '2025-01-01 01:00:00'),
                (3,3, 0, '2025-01-01 01:00:00'),
                (4,4, 0, '2025-01-01 01:00:00'),
                (5,3, 100, '2025-02-01 08:00:00'),
                (6,4, 50, '2025-02-01 08:00:00'),
                (7,1, 10000, '2025-02-01 08:00:00'),
                (8,2, 20, '2025-02-01 08:00:00')
        on conflict do nothing;

--insert into stock_out (ingredient_id, quantity, last_modified)
--     values  (1, 0, '2025-01-01 01:00:00'),
--             (2, 0, '2025-01-01 01:00:00'),
--            (3, 0, '2025-01-01 01:00:00'),
--             (4, 0, '2025-01-01 01:00:00');

INSERT INTO stock_out (id,ingredient_id, quantity, last_modified)
       VALUES  (1,1, 0, '2025-01-01 01:00:00'),
               (2,2, 0, '2025-01-01 01:00:00'),
               (3,3, 0, '2025-01-01 01:00:00'),
               (4,4, 0, '2025-01-01 01:00:00'),
               (5,3, 10, '2025-02-02 10:00:00'),
               (6,3, 10, '2025-02-03 15:00:00'),
               (7,4, 20, '2025-02-05 16:00:00')
        on conflict do nothing;

--with so as (select ingredient_id, sum(quantity) as available from stock_out group by ingredient_id),
--si as (select ingredient_id, sum(quantity) as available from stock_in group by ingredient_id)
--select si.ingredient_id, (si.available - so.available) as quantity from si
--join so on si.ingredient_id=so.ingredient_id;
