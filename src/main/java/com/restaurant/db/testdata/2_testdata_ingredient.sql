insert into ingredient (ingredient_id, ingredient_name, unit, last_modified)
        values  (1,'sausage','G', '2025-01-01 00:00:00'),
                (2,'oil','L', '2025-01-01 00:00:00'),
                (3,'egg','U', '2025-01-01 00:00:00'),
                (4,'bread','U', '2025-01-01 00:00:00')
        on conflict do nothing;

