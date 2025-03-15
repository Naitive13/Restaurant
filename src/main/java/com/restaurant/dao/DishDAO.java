package com.restaurant.dao;

import com.restaurant.db.Datasource;
import com.restaurant.entities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DishDAO implements CrudDAO<Dish> {
  private final Datasource datasource;

  public DishDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  public DishDAO() {
    this.datasource = new Datasource();
  }

  @Override
  public List<Dish> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }

    List<Dish> result = new ArrayList<>();
    List<Criteria> ingredientCriteria = new ArrayList<>();
    IngredientDAO ingredientDAO = new IngredientDAO();

    String query = "SELECT dish_id, dish_name, dish_price FROM dish WHERE 1=1";
    String orderBy = " ORDER BY dish_id ASC";
    String dishName = null;
    long dishId = 0,
        dishIdIndex = 0,
        dishNameIndex = 0,
        dishPrice = 0,
        dishPriceIndex = 0,
        counter = 1;

    for (Criteria criteria : criteriaList) {
      if (criteria.getClass() == Filter.class) {
        switch (criteria.getColumn()) {
          case "dish_id" -> {
            dishId = (long) ((Filter) criteria).getValue();
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
            ingredientCriteria.add(criteria);
          }
        }
      } else {
        String column = criteria.getColumn();
        SortOrder sortOrder = ((Sort) criteria).getOrder();
        orderBy = "ORDER BY " + column + " " + sortOrder;
      }
    }
    query += orderBy + " LIMIT ? OFFSET ?";

    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);
      if (dishIdIndex != 0) st.setLong((int) (dishIdIndex), dishId);
      if (dishNameIndex != 0) st.setString((int) (dishNameIndex), dishName);
      if (dishPriceIndex != 0) st.setLong((int) (dishPriceIndex), dishPrice);
      st.setInt((int) (counter), pageSize);
      st.setInt((int) (counter + 1), (pageIndex - 1) * pageSize);

      ResultSet rs = st.executeQuery();

      while (rs.next()) {
        dishId = rs.getLong("dish_id");
        Filter dishIngredientFilter = new Filter("dish_id", dishId);
        ingredientCriteria.add(dishIngredientFilter);

        List<Ingredient> ingredientList = ingredientDAO.get(ingredientCriteria, 1, pageSize);
        Dish dish =
            new Dish(dishId, rs.getString("dish_name"), rs.getLong("dish_price"), ingredientList);
        result.add(dish);
        ingredientCriteria.removeLast();
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Dish newDish) {
    String query =
        "INSER INTO dish (dish_id,dish_name,dish_price) "
            + "VALUES (?, ?, ?) "
            + "ON CONFLICT (dish_name,dish_id) "
            + "DO NOTHING";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
