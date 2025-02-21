package com.restaurant.dao;

import com.restaurant.db.Datasource;
import com.restaurant.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {
    private Datasource datasource;

    public DishDAO(Datasource datasource) {
        this.datasource = datasource;
    }

    public DishDAO() {
        this.datasource = new Datasource();
    }

    public List<Dish> getAll(List<Criteria> criteriaList, int pageIndex, int pageSize){
        if (pageIndex < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + pageIndex);
        }

        String query = "SELECT dish_id, dish_name, dish_price FROM dish WHERE 1=1";
        String orderBy = " ORDER BY dish_id ASC";
        String dishName = null;
        int dishId = 0, dishIdIndex = 0, dishNameIndex = 0, dishPrice = 0, dishPriceIndex = 0, counter = 1;

        for (Criteria criteria : criteriaList) {
            if (criteria.getClass() == Filter.class){
                switch (criteria.getColumn()) {
                    case "dish_id" ->{
                        dishId = (int) ((Filter) criteria).getValue();
                        if (counter == 1) {
                            query = query + " AND dish_id=? ";
                        } else {
                            query = query + " OR dish_id=? ";
                        }
                        dishIdIndex = counter;
                        counter++;
                    }
                    case "dish_price" ->{
                        dishPrice = (int) ((Filter) criteria).getValue();
                        if (counter == 1) {
                            query = query + " AND dish_price=? ";
                        } else {
                            query = query + " OR dish_price=? ";
                        }
                        dishPriceIndex = counter;
                        counter++;
                    }
                    case "dish_name" ->{
                        dishName = (String) ((Filter) criteria).getValue();
                        if (counter == 1) {
                            query = query + " AND dish_name ILIKE ? ";
                        } else {
                            query = query + " OR dish_name ILIKE ? ";
                        }
                        dishNameIndex = counter;
                        counter++;
                    }
                    default -> {
                        throw new RuntimeException("invalid column name");
                    }
                }
            } else {
                String column = criteria.getColumn();
                Order order = ((Sort) criteria).getOrder();
                orderBy = "ORDER BY " + column + " " + order;
            }
        }
        query += orderBy + " LIMIT ? OFFSET ?";

        try (Connection connection = this.datasource.getConnection();
        PreparedStatement st = connection.prepareStatement(query);
        ){
            if (dishIdIndex != 0) st.setInt(dishIdIndex, dishId);
            if (dishNameIndex != 0) st.setString(dishNameIndex, dishName);
            if (dishPriceIndex != 0) st.setInt(dishPriceIndex, dishPrice);
            st.setInt( counter, pageSize);
            st.setInt(counter +1, (pageIndex - 1) * pageSize);

            ResultSet rs = st.executeQuery();
            List<Dish> result = new ArrayList<>();

            while(rs.next()){
                List<Ingredient> ingredientList= new ArrayList<>();
                PreparedStatement st2 = connection.prepareStatement("SELECT dish.dish_id, dish.dish_name, ingredient.ingredient_name," +
                        "ingredient.ingredient_id, ingredient_price.unit_price, ingredient.unit,"+
                        " dish_ingredient.quantity FROM dish_ingredient  " +
                        "JOIN dish ON dish_ingredient.dish_id=dish.dish_id  " +
                        "JOIN ingredient ON dish_ingredient.ingredient_id=ingredient.ingredient_id  " +
                        "JOIN ingredient_price ON ingredient.ingredient_id=ingredient_price.ingredient_id ;");
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()){
                    ingredientList.add(new Ingredient(
                            rs2.getInt("ingredient_id"),
                            rs2.getString("ingredient_name"),
                            UnitType.valueOf(rs2.getString("unit")),
                            rs2.getInt("unit_price"),
                            rs2.getFloat("quantity")
                    ));
                }
                result.add(new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        rs.getInt("dish_price"),
                        ingredientList
                ));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
