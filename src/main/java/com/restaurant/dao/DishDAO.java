package com.restaurant.dao;

import com.restaurant.db.Datasource;
import com.restaurant.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {
    private final Datasource datasource;

    public DishDAO(Datasource datasource) {
        this.datasource = datasource;
    }

    public DishDAO() {
        this.datasource = new Datasource();
    }

    public List<Dish> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
        if (pageIndex < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + pageIndex);
        }

        String query = "SELECT dish_id, dish_name, dish_price FROM dish WHERE 1=1";
        String orderBy = " ORDER BY dish_id ASC";
        String dishName = null;
        long dishId = 0, dishIdIndex = 0, dishNameIndex = 0, dishPrice = 0, dishPriceIndex = 0, counter = 1;

        for (Criteria criteria : criteriaList) {
            if (criteria.getClass() == Filter.class) {
                switch (criteria.getColumn()) {
                    case "dish_id" -> {
                        dishId = (int) ((Filter) criteria).getValue();
                            query = query + " AND dish_id=? ";

                        dishIdIndex = counter;
                        counter++;
                    }
                    case "dish_price" -> {
                        dishPrice = (long) ((Filter) criteria).getValue();
                            query = query + " AND dish_price=? ";
                        dishPriceIndex = counter;
                        counter++;
                    }
                    case "dish_name" -> {
                        dishName = (String) ((Filter) criteria).getValue();
                            query = query + " AND dish_name ILIKE ? ";
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
        ) {
            PreparedStatement st = connection.prepareStatement(query);
            if (dishIdIndex != 0) st.setLong((int)(dishIdIndex), dishId);
            if (dishNameIndex != 0) st.setString((int)(dishNameIndex), dishName);
            if (dishPriceIndex != 0) st.setLong((int)(dishPriceIndex), dishPrice);
            st.setInt((int)(counter), pageSize);
            st.setInt((int)(counter + 1), (pageIndex - 1) * pageSize);

            ResultSet rs = st.executeQuery();
            List<Dish> result = new ArrayList<>();

            while (rs.next()) {
                List<Ingredient> ingredientList = new ArrayList<>();
                PreparedStatement st2 = connection.prepareStatement("SELECT dish.dish_name, ingredient.ingredient_name," +
                        "ingredient.ingredient_id, ingredient_price.unit_price, ingredient.unit," +
                        " dish_ingredient.quantity FROM dish_ingredient  " +
                        "JOIN dish ON dish_ingredient.dish_id=dish.dish_id  " +
                        "JOIN ingredient ON dish_ingredient.ingredient_id=ingredient.ingredient_id  " +
                        "JOIN ingredient_price ON ingredient.ingredient_id=ingredient_price.ingredient_id ;");
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()) {
                    List<Price> priceList = new ArrayList<>();
                    long ingredientId = rs2.getLong("ingredient_id");
                    PreparedStatement st3 = connection.prepareStatement(
                            "SELECT ingredient_id, unit_price, price_date " +
                            "FROM ingredient_price "+
                            "WHERE ingredient_id=?");
                    st3.setLong(1,ingredientId);
                    ResultSet rs3 = st3.executeQuery();
                    while (rs3.next()){
                        Price price = new Price(
                                ingredientId,
                                rs3.getLong("unit_price"),
                                rs3.getTimestamp("price_date").toLocalDateTime()
                        );
                        priceList.add(price);
                    }
                    ingredientList.add(new Ingredient(
                            ingredientId,
                            rs2.getString("ingredient_name"),
                            UnitType.valueOf(rs2.getString("unit")),
                            priceList,
                            rs2.getDouble("quantity")
                    ));
                }
                result.add(new Dish(
                        rs.getLong("dish_id"),
                        rs.getString("dish_name"),
                        rs.getLong("dish_price"),
                        ingredientList
                ));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save (Dish newDish){
        String query = "INSER INTO dish (dish_id,dish_name,dish_price) "+
                "VALUES (?, ?, ?) "+
                "ON CONFLICT (dish_name,dish_id) "+
                "DO UPDATE SET";
        try(Connection connection = this.datasource.getConnection()){
            PreparedStatement st = connection.prepareStatement(query);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
