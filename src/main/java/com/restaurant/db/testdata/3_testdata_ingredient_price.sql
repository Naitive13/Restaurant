insert into ingredient_price (id,ingredient_id, unit_price, price_date)
        values  (1,1, 20, '2025-01-01 00:00:00'),
                (2,2, 10000, '2025-01-01 00:00:00'),
                (3,3, 1000, '2025-01-01 00:00:00'),
                (4,4, 1000, '2025-01-01 00:00:00')
        on conflict do nothing;

