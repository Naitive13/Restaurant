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

public class OrderDAO implements CrudDAO<Order> {
  private final Datasource datasource;

  public OrderDAO() {
    this.datasource = new Datasource();
  }

  public OrderDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  @Override
  public List<Order> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }
    List<Order> result = new ArrayList<>();
    List<Criteria> statusCriteria = new ArrayList<>();
    DishOrderDAO dishOrderDAO = new DishOrderDAO();
    OrderStatusDAO orderStatusDAO = new OrderStatusDAO();

    String query = "SELECT order_reference, creation_date " + "FROM \"order\" WHERE 1=1 ";
    String orderBy = " ORDER BY order_reference ASC";

    String orderReference = null;
    LocalDateTime dateTime1 = null, dateTime2 = null;
    long orderReferenceIndex = 0, dateTime1Index = 0, dateTime2Index = 0, counter = 1;

    for (Criteria criteria : criteriaList) {
      if (criteria.getClass() == Filter.class) {
        switch (criteria.getColumn()) {
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

          case "order_reference" -> {
            orderReference = (String) ((Filter) criteria).getValue();
            query = query + " AND order_reference ILIKE ? ";
            orderReferenceIndex = counter;
            counter++;
          }
          default -> {
            statusCriteria.add(criteria);
          }
        }
      } else {
        String column = criteria.getColumn();
        SortOrder sortOrder = ((Sort) criteria).getSortOrder();
        orderBy = "ORDER BY " + column + " " + sortOrder;
      }
    }
    query += orderBy + " LIMIT ? OFFSET ?";

    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);
      if (orderReferenceIndex != 0) st.setString((int) (orderReferenceIndex), orderReference);
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
        String reference = rs.getString("order_reference");
        Filter dishOrderFilter = new Filter("order_reference", reference);
        statusCriteria.add(dishOrderFilter);

        List<DishOrder> dishOrderList = dishOrderDAO.get(statusCriteria, pageIndex, pageSize);

        List<Status> statusList = orderStatusDAO.get(statusCriteria, 1, 10);

        Order order =
            new Order(
                reference,
                rs.getTimestamp("creation_date").toLocalDateTime(),
                dishOrderList,
                statusList);
        result.add(order);
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Order element) {
    String query =
            "INSERT INTO \"order\" "
                    + "(order_reference,creation_date) "
                    + "VALUES (?,?)"
                    + "ON CONFLICT DO NOTHING";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);

      st.setString(1, element.getReference());
      st.setTimestamp(2, Timestamp.valueOf(element.getCreationDate()));

      st.executeUpdate();

      OrderStatusDAO orderStatusDAO = new OrderStatusDAO();
      element.getStatusList().forEach(status->{
        orderStatusDAO.save(status,element.getReference());
      });
      DishOrderDAO dishOrderDAO = new DishOrderDAO();
      element.getDishOrderList().forEach(dishOrderDAO::save);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
