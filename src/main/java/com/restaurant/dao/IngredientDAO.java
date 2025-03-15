package com.restaurant.dao;

import com.restaurant.db.Datasource;
import com.restaurant.entities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO implements CrudDAO<Ingredient> {
  private final Datasource datasource;

  public IngredientDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  public IngredientDAO() {
    this.datasource = new Datasource();
  }

  @Override
  public List<Ingredient> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }

    List<Ingredient> result = new ArrayList<>();
    List<Criteria> priceCriteria = new ArrayList<>();
    PriceDAO priceDAO = new PriceDAO();

    String query =
        "SELECT ingredient.ingredient_name, ingredient.ingredient_id, ingredient.unit,"
            + " ingredient.last_modified, dish_ingredient.quantity FROM dish_ingredient  "
            + "RIGHT JOIN ingredient ON dish_ingredient.ingredient_id=ingredient.ingredient_id "
            + "WHERE 1=1 ";
    String orderBy = " ORDER BY ingredient.ingredient_id ASC";
    LocalDateTime dateTime1 = null, dateTime2 = null;
    String ingredientName = null;
    UnitType ingredientUnit = null;
    long ingredientId = 0,
        ingredientIdIndex = 0,
        ingredientNameIndex = 0,
        dateTime1Index = 0,
        dateTime2Index = 0;
    long dishId = 0, dishIdIndex = 0, ingredientUnitIndex = 0, counter = 1;

    for (Criteria criteria : criteriaList) {
      if (criteria.getClass() == Filter.class) {
        switch (criteria.getColumn()) {
          case "dish_id" -> {
            dishId = (long) ((Filter) criteria).getValue();
            query = query + " AND dish_ingredient.dish_id=? ";
            dishIdIndex = counter;
            counter++;
          }
          case "last_modified" -> {
            DateTimeInterval dateTimeInterval = (DateTimeInterval) ((Filter) criteria).getValue();
            dateTime1 = dateTimeInterval.getDate1();
            dateTime2 = dateTimeInterval.getDate2();
            query = query + " AND ingredient.last_modified BETWEEN ? and ? ";
            dateTime1Index = counter;
            counter++;
            dateTime2Index = counter;
            counter++;
          }
          case "ingredient_id" -> {
            ingredientId = (long) ((Filter) criteria).getValue();
            query = query + " AND ingredient.ingredient_id=? ";
            ingredientIdIndex = counter;
            counter++;
          }
          case "ingredient_name" -> {
            ingredientName = (String) ((Filter) criteria).getValue();
            query = query + " AND ingredient.ingredient_name ILIKE ? ";
            ingredientNameIndex = counter;
            counter++;
          }
          case "unit" -> {
            ingredientUnit = (UnitType) ((Filter) criteria).getValue();
            query = query + " AND ingredient.unit=?::unit_type ";
            ingredientUnitIndex = counter;
            counter++;
          }
          default -> {
            priceCriteria.add(criteria);
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
      if (dateTime1Index != 0) {
        st.setTimestamp((int) (dateTime1Index), Timestamp.valueOf(dateTime1));
        st.setTimestamp((int) (dateTime2Index), Timestamp.valueOf(dateTime2));
      }
      if (ingredientIdIndex != 0) st.setLong((int) (ingredientIdIndex), ingredientId);
      if (ingredientNameIndex != 0) {
        st.setString((int) (ingredientNameIndex), "%" + ingredientName + "%");
      }
      if (ingredientUnitIndex != 0) {
        st.setString((int) (ingredientUnitIndex), ingredientUnit.name());
      }
      st.setInt((int) (counter), pageSize);
      st.setInt((int) (counter + 1), (pageIndex - 1) * pageSize);

      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        ingredientId = rs.getLong("ingredient_id");
        Filter ingredientPriceFilter = new Filter("ingredient_id", ingredientId);
        priceCriteria.add(ingredientPriceFilter);

        List<Price> priceList = priceDAO.get(priceCriteria, 1, pageSize);
        Ingredient ingredient =
            new Ingredient(
                ingredientId,
                rs.getString("ingredient_name"),
                UnitType.valueOf(rs.getString("unit")),
                priceList,
                rs.getDouble("quantity"),
                rs.getTimestamp("last_modified").toLocalDateTime());
        result.add(ingredient);
        priceCriteria.removeLast();
      }
      return result;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Ingredient ingredient) {
    String query =
        "INSERT INTO ingredient "
            + "(ingredient_id, ingredient_name, unit, last_modified) "
            + "VALUES (?,?,?::unit_type,?)"
            + "ON CONFLICT DO NOTHING";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);

      st.setLong(1, ingredient.getIngredientId());
      st.setString(2, ingredient.getIngredientName());
      st.setString(3, ingredient.getUnit().toString());
      st.setTimestamp(4, Timestamp.valueOf(ingredient.getLastModified()));
      int rs = st.executeUpdate();

      PriceDAO priceDAO = new PriceDAO();
      ingredient.getIngredientPrices().forEach(priceDAO::save);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
