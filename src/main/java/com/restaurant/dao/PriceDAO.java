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

public class PriceDAO implements CrudDAO<Price> {
  private final Datasource datasource;

  public PriceDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  public PriceDAO() {
    this.datasource = new Datasource();
  }

  @Override
  public List<Price> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }

    List<Price> result = new ArrayList<>();
    String query =
        "SELECT ingredient_id, unit_price, price_date " + "FROM ingredient_price " + "WHERE 1=1 ";
    String orderBy = " ORDER BY price_date DESC";
    LocalDateTime dateTime1 = null, dateTime2 = null;
    long ingredientId = 0,
        ingredientIdIndex = 0,
        unitPrice = 0,
        unitPriceIndex = 0,
        counter = 1,
        dateTime1Index = 0,
        dateTime2Index = 0;

    for (Criteria criteria : criteriaList) {
      if (criteria.getClass() == Filter.class) {
        switch (criteria.getColumn()) {
          case "ingredient_id" -> {
            ingredientId = (long) ((Filter) criteria).getValue();
            query = query + " AND ingredient_id=? ";
            ingredientIdIndex = counter;
            counter++;
          }
          case "unit_price" -> {
            unitPrice = (long) ((Filter) criteria).getValue();
            query = query + " AND unit_price=? ";
            unitPriceIndex = counter;
            counter++;
          }
          case "price_date" -> {
            DateTimeInterval dateTimeInterval = (DateTimeInterval) ((Filter) criteria).getValue();
            dateTime1 = dateTimeInterval.getDate1();
            dateTime2 = dateTimeInterval.getDate2();
            query = query + " AND price_date BETWEEN ? and ? ";
            dateTime1Index = counter;
            counter++;
            dateTime2Index = counter;
            counter++;
          }
          default -> {
            throw new RuntimeException("invalid column name");
          }
        }
      }
    }
    query += orderBy + " LIMIT ? OFFSET ?";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);
      if (ingredientIdIndex != 0) st.setLong((int) (ingredientIdIndex), ingredientId);
      if (unitPriceIndex != 0) {
        st.setLong((int) (unitPriceIndex), unitPrice);
      }
      if (dateTime1Index != 0) {
        st.setTimestamp((int) (dateTime1Index), Timestamp.valueOf(dateTime1));
        st.setTimestamp((int) (dateTime2Index), Timestamp.valueOf(dateTime2));
        st.setInt((int) (counter), pageSize);
        st.setInt((int) (counter + 1), (pageIndex - 1) * pageSize);
      } else {
        st.setInt((int) (counter), 1);
        st.setInt((int) (counter + 1), 0);
      }
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        Price price =
            new Price(
                rs.getLong("ingredient_id"),
                rs.getLong("unit_price"),
                rs.getTimestamp("price_date").toLocalDateTime());
        result.add(price);
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Price element) {
    throw new RuntimeException("not implemented yet sorry...");
  }
}
