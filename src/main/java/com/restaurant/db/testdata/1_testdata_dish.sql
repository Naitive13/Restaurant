insert into dish (dish_id,dish_name, dish_price)
        values (1,'hotdog', 15000)
        on conflict do nothing;

