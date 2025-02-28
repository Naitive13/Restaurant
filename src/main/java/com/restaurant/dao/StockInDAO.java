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

public class StockInDAO implements CrudDAO<Stock> {
  private final Datasource datasource;

  public StockInDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  public StockInDAO() {
    this.datasource = new Datasource();
  }

  @Override
  public List<Stock> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }

    List<Stock> result = new ArrayList<>();
    String query =
        "SELECT stock_in.ingredient_id, stock_in.quantity, "
            + "ingredient.unit, stock_in.last_modified FROM stock_in "
            + "JOIN ingredient ON ingredient.ingredient_id=stock_in.ingredient_id WHERE 1=1 ";
    String orderBy = "ORDER BY stock_in.last_modified ASC";
    String unit = null;
    LocalDateTime dateTime1 = null, dateTime2 = null;
    long unitIndex = 0,
        quantity = 0,
        quantityIndex = 0,
        ingredientId = 0,
        ingredientIdIndex = 0,
        dateTime2Index = 0,
        dateTime1Index = 0,
        counter = 1;
    for (Criteria criteria : criteriaList) {
      if (criteria.getClass() == Filter.class) {
        switch (criteria.getColumn()) {
          case "unit" -> {
            unit = ((Filter) criteria).getValue().toString();
            query = query + " AND ingredient.unit=?::unit_type ";
            unitIndex = counter;
            counter++;
          }
          case "quantity" -> {
            quantity = (long) ((Filter) criteria).getValue();
            query = query + " AND stock_in.quantity=? ";
            quantityIndex = counter;
            counter++;
          }
          case "ingredient_id" -> {
            ingredientId = (long) ((Filter) criteria).getValue();
            query = query + " AND stock_in.ingredient_id=? ";
            ingredientIdIndex = counter;
            counter++;
          }
          case "last_modified" -> {
            DateTimeInterval dateTimeInterval = (DateTimeInterval) ((Filter) criteria).getValue();
            dateTime1 = dateTimeInterval.getDate1();
            dateTime2 = dateTimeInterval.getDate2();
            query = query + " AND stock_in.last_modified BETWEEN ? and ? ";
            dateTime1Index = counter;
            counter++;
            dateTime2Index = counter;
            counter++;
          }
          default -> {
            throw new RuntimeException();
          }
        }
      } else {
        String column = criteria.getColumn();
        Order order = ((Sort) criteria).getOrder();
        orderBy = "ORDER BY " + column + " " + order;
      }
    }
    query += orderBy + " LIMIT ? OFFSET ?";

    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);
      if (unitIndex != 0) st.setString((int) unitIndex, unit);
      if (quantityIndex != 0) st.setLong((int) quantityIndex, quantity);
      if (ingredientIdIndex != 0) st.setLong((int) (ingredientIdIndex), ingredientId);
      if (dateTime1Index != 0) {
        st.setTimestamp((int) (dateTime1Index), Timestamp.valueOf(dateTime1));
        st.setTimestamp((int) (dateTime2Index), Timestamp.valueOf(dateTime2));
      }
      st.setInt((int) (counter), pageSize);
      st.setInt((int) (counter + 1), (pageIndex - 1) * pageSize);

      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        Stock stock =
            new Stock(
                rs.getLong("ingredient_id"),
                rs.getDouble("quantity"),
                UnitType.valueOf(rs.getString("unit")),
                rs.getTimestamp("last_modified").toLocalDateTime(),
                StockType.IN);
        result.add(stock);
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public double getTotalForIngredient(long ingredientId) {
    String query =
        "SELECT ingredient_id, sum(quantity) as total "
            + "FROM stock_in GROUP BY ingredient_id "
            + "HAVING ingredient_id=?";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);
      st.setLong(1, ingredientId);
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        return rs.getDouble("total");
      }
      throw new RuntimeException("ingredient not found");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public double getTotalForIngredient(long ingredientId, LocalDateTime dateTime) {
    String query =
    "WITH stock AS (SELECT ingredient_id, quantity FROM "
            + "stock_in WHERE last_modified <= ?) "
            + "SELECT ingredient_id, sum(quantity) as total "
            + "FROM stock GROUP BY ingredient_id "
            + "HAVING ingredient_id = ? ";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);
      st.setTimestamp(1, Timestamp.valueOf(dateTime));
      st.setLong(2, ingredientId);
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        return rs.getDouble("total");
      }
      throw new RuntimeException("ingredient not found");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Stock element) {
    throw new RuntimeException("not implemented yet sorry...");
  }
}
