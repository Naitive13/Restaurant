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

public class DishOrderStatusDAO {
  private final Datasource datasource;

  public DishOrderStatusDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  public DishOrderStatusDAO() {
    this.datasource = new Datasource();
  }

  public List<Status> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }

    List<Status> result = new ArrayList<>();
    String query =
        "SELECT dish_order_id, dish_order_status, creation_date "
            + "FROM dish_order_status "
            + "WHERE 1=1 ";
    String orderBy = " ORDER BY id ASC";
    LocalDateTime dateTime1 = null, dateTime2 = null;
    StatusType dishOrderStatusType = null;
    long dishOrderId = 0,
        dishOrderIdIndex = 0,
        dishOrderStatusIndex = 0,
        counter = 1,
        dateTime1Index = 0,
        dateTime2Index = 0;

    for (Criteria criteria : criteriaList) {
      if (criteria.getClass() == Filter.class) {
        switch (criteria.getColumn()) {
          case "dish_order_id" -> {
            dishOrderId = (long) ((Filter) criteria).getValue();
            query = query + " AND dish_order_id=? ";
            dishOrderIdIndex = counter;
            counter++;
          }
          case "dish_order_status" -> {
            dishOrderStatusType = (StatusType) ((Filter) criteria).getValue();
            query = query + " AND dish_order_status=?::status ";
            dishOrderStatusIndex = counter;
            counter++;
          }
          case "creation_date" -> {
            DateTimeInterval dateTimeInterval = (DateTimeInterval) ((Filter) criteria).getValue();
            dateTime1 = dateTimeInterval.getDate1();
            dateTime2 = dateTimeInterval.getDate2();
            query = query + " AND creation_date BETWEEN ? and ? ";
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
      if (dishOrderIdIndex != 0) st.setLong((int) (dishOrderIdIndex), dishOrderId);
      if (dishOrderStatusIndex != 0) {
        st.setString((int) (dishOrderStatusIndex), dishOrderStatusType.name());
      }
      if (dateTime1Index != 0) {
        st.setTimestamp((int) (dateTime1Index), Timestamp.valueOf(dateTime1));
        st.setTimestamp((int) (dateTime2Index), Timestamp.valueOf(dateTime2));
      }
      st.setInt((int) (counter), pageSize);
      st.setInt((int) (counter + 1), (pageIndex - 1) * pageSize);

      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        Status newStatus =
            new Status(
                StatusType.valueOf(rs.getString("dish_order_status")),
                rs.getTimestamp("creation_date").toLocalDateTime());
        result.add(newStatus);
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void save(Status status, long dishOrderId) {
    String query =
        "INSERT INTO dish_order_status "
            + "(dish_order_id, dish_order_status, creation_date) "
            + "VALUES (?,?::statusType,?)"
    + "ON CONFLICT (dish_order_id, dish_order_status) DO NOTHING";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);

      st.setLong(1, dishOrderId);
      st.setString(2, status.getStatusType().name());
      st.setTimestamp(3, Timestamp.valueOf(status.getDateTime()));

      st.executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
