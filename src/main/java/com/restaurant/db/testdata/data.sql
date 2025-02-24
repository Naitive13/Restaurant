INSERT INTO dish (dish_id,dish_name, dish_price)
        VALUES (1,'hotdog', 15000);

INSERT INTO ingredient (ingredient_id, ingredient_name, unit)
        VALUES  (1,'sausage','G'),
                (2,'oil','L'),
                (3,'egg','U'),
                (4,'bread','U');

INSERT INTO ingredient_price (ingredient_id, unit_price, price_date)
        VALUES  (1, 20, '2025-01-01 00:00:00'),
                (2, 10000, '2025-01-01 00:00:00'),
                (3, 1000, '2025-01-01 00:00:00'),
                (4, 1000, '2025-01-01 00:00:00');

INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity)
        VALUES  (1,1,100),
                (1,2,0.15),
                (1,3,1),
                (1,4,1);