INSERT INTO dish (dish_name, dish_price)
        VALUES ('hotdog', 15000);

INSERT INTO ingredient (ingredient_name, unit)
        VALUES  ('sausage','G'),
                ('oil','L'),
                ('egg','U'),
                ('bread','U');

INSERT INTO ingredient_price (ingredient_id, unit_price, price_date)
        VALUES  (1, 20, CURRENT_TIMESTAMP),
                (2, 10000, CURRENT_TIMESTAMP),
                (3, 1000, CURRENT_TIMESTAMP),
                (4, 1000, CURRENT_TIMESTAMP);

INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity)
        VALUES  (1,1,100),
                (1,2,0.15),
                (1,3,1),
                (1,4,1);