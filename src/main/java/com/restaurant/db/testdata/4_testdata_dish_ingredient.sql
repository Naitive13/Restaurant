insert into dish_ingredient (id,dish_id, ingredient_id, quantity)
        values  (1,1,1,100),
                (2,1,2,0.15),
                (3,1,3,1),
                (4,1,4,1)
        on conflict do nothing;
