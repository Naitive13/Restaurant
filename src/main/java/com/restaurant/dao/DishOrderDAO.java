package com.restaurant.dao;

import com.restaurant.db.Datasource;
import com.restaurant.entities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DishOrderDAO implements CrudDAO<DishOrder> {
  private final Datasource datasource;

  public DishOrderDAO(Datasource datasource) {
    this.datasource = datasource;
  }

  public DishOrderDAO() {
    this.datasource = new Datasource();
  }

  @Override
  public List<DishOrder> get(List<Criteria> criteriaList, int pageIndex, int pageSize) {
    if (pageIndex < 1) {
      throw new IllegalArgumentException(
          "page index must be greater than 0 but actual is " + pageIndex);
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException(
          "page size must be greater than 0 but actual is " + pageIndex);
    }
    List<DishOrder> result = new ArrayList<>();
    List<Criteria> dishCriteria = new ArrayList<>();
    List<Criteria> dishOrderStatusCriteria = new ArrayList<>();
    DishDAO dishDAO = new DishDAO();
    DishOrderStatusDAO dishOrderStatusDAO = new DishOrderStatusDAO();

    String query = "SELECT dish_order_id, dish_id, quantity, order_reference " + "FROM dish_order WHERE 1=1 ";
    String orderBy = " ORDER BY dish_order_id ASC";

    String orderReference = null;
    long dishId = 0,
        dishIdIndex = 0,
        dishOrderId = 0,
        dishOrderIdIndex = 0,
        orderReferenceIndex = 0,
        quantity = 0,
        quantityIndex = 0,
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

          case "quantity" -> {
            quantity = (long) ((Filter) criteria).getValue();
            query = query + " AND quantity=? ";
            quantityIndex = counter;
            counter++;
          }

          case "dishOrderId" -> {
            dishOrderId = (long) ((Filter) criteria).getValue();
            query = query + " AND dish_order_id=? ";
            dishOrderIdIndex = counter;
            counter++;
          }
          case "order_reference" -> {
            orderReference = (String) ((Filter) criteria).getValue();
            query = query + " AND order_reference ILIKE ? ";
            orderReferenceIndex = counter;
            counter++;
          }
          default -> {
            dishCriteria.add(criteria);
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
      if (dishIdIndex != 0) st.setLong((int) (dishIdIndex), dishId);
      if (dishOrderIdIndex != 0) st.setLong((int) (dishOrderIdIndex), dishOrderId);
      if (orderReferenceIndex != 0) st.setString((int) (orderReferenceIndex), orderReference);
      if (quantityIndex != 0) st.setLong((int) (quantityIndex), quantity);
      st.setInt((int) (counter), pageSize);
      st.setInt((int) (counter + 1), (pageIndex - 1) * pageSize);

      ResultSet rs = st.executeQuery();

      while (rs.next()) {
        dishId = rs.getLong("dish_id");
        Filter dishFilter = new Filter("dish_id", dishId);
        dishCriteria.add(dishFilter);
        Dish dish = dishDAO.get(dishCriteria, pageIndex, pageSize).getFirst();

        dishOrderId = rs.getLong("dish_order_id");
        Filter dishOrderStatusFilter = new Filter("dish_order_id", dishOrderId);
        dishOrderStatusCriteria.add(dishOrderStatusFilter);
        List<Status> statusList =
            dishOrderStatusDAO.get(dishOrderStatusCriteria, pageIndex, pageSize);

        DishOrder dishOrder = new DishOrder(dishOrderId,dish, rs.getString("order_reference"), rs.getLong("quantity"), statusList);
        result.add(dishOrder);
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(DishOrder dishOrder) {
    String query =
            "INSERT INTO dish_order "
                    + "(dish_order_id, dish_id, order_reference, quantity) "
                    + "VALUES (?,?,?,?)"
                    + "ON CONFLICT DO NOTHING";
    try (Connection connection = this.datasource.getConnection()) {
      PreparedStatement st = connection.prepareStatement(query);

      st.setLong(1, dishOrder.getId());
      st.setLong(2, dishOrder.getDish().getDishId());
      st.setString(3, dishOrder.getOrderReference());
      st.setLong(4,dishOrder.getQuantity());

      st.executeUpdate();
      DishOrderStatusDAO dishOrderStatusDAO =new DishOrderStatusDAO();
      dishOrder.getStatusList().forEach(status -> {
        dishOrderStatusDAO.save(status,dishOrder.getId());
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
