import static org.junit.jupiter.api.Assertions.assertEquals;

import com.restaurant.dao.DishDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.entities.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {
  OrderDAO subject = new OrderDAO();
  DishDAO dishDAO = new DishDAO();

  private Order newOrder() {
    DishOrder dishOrder =
        new DishOrder(1, dishDAO.get(new ArrayList<>(), 1, 10).getFirst(), "ORD002", 2);
    Status status = new Status(StatusType.CREATED, LocalDateTime.of(2025, 1, 1, 0, 0, 0));
    List<Status> statusList = new ArrayList<>();
    statusList.add(status);
    List<DishOrder> dishOrderList = new ArrayList<>();
    dishOrderList.add(dishOrder);
    return new Order("ORD002", LocalDateTime.of(2025, 1, 1, 0, 0, 0), dishOrderList, statusList);
  }

  @Test
  @org.junit.jupiter.api.Order(1)
  void add_new_order() {
    Order expected = newOrder();

    subject.save(expected);
    Order actual = subject.get(new ArrayList<>(), 1, 10).getFirst();

    assertEquals(expected, actual);
  }

  @Test
  @org.junit.jupiter.api.Order(2)
  void update_status() {
    Status expected = new Status(StatusType.CONFIRMED, LocalDateTime.of(2025, 2, 1, 0, 0, 0));

    Order order = newOrder();
    order.updateStatus();
    Status actual = order.getActualStatus();

    assertEquals(expected, actual);
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  void get_total_amount() {
    Order order = newOrder();
    long expected = 30_000;

    long actual = order.getTotalAmount();

    assertEquals(expected, actual);
  }
}
