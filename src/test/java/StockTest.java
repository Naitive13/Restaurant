import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.restaurant.dao.StockInDAO;
import com.restaurant.dao.StockOutDAO;
import com.restaurant.entities.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StockTest {
  private final StockInDAO subject1 = new StockInDAO();
  private final StockOutDAO subject2 = new StockOutDAO();
  private final LocalDateTime initialDateTime = LocalDateTime.of(2025, 1, 1, 1, 0, 0);

  @Test
  void get_stock_movement_in() {
    List<Stock> expected = fakeStockIn();

    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("quantity", Order.ASC));
    List<Stock> actual = subject1.get(criteria, 1, 10);

    assertEquals(expected, actual);
  }

  @Test
  void get_stock_movement_out() {
    List<Stock> expected = fakeStockOut();

    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("ingredient_id", Order.ASC));
    List<Stock> actual = subject2.get(criteria, 1, 10);

    assertEquals(expected, actual);
  }

  private List<Stock> fakeStockIn() {
    List<Stock> result = new ArrayList<>();
    final LocalDateTime restockDateTime = LocalDateTime.of(2025, 2, 1, 8, 0, 0);
    result.add(new Stock(1, 0d, UnitType.G, initialDateTime, StockType.IN));
    result.add(new Stock(2, 0d, UnitType.L, initialDateTime, StockType.IN));
    result.add(new Stock(3, 0d, UnitType.U, initialDateTime, StockType.IN));
    result.add(new Stock(4, 0d, UnitType.U, initialDateTime, StockType.IN));
    result.add(new Stock(2, 20d, UnitType.L, restockDateTime, StockType.IN));
    result.add(new Stock(4, 50d, UnitType.U, restockDateTime, StockType.IN));
    result.add(new Stock(3, 100d, UnitType.U, restockDateTime, StockType.IN));
    result.add(new Stock(1, 10_000d, UnitType.G, restockDateTime, StockType.IN));
    result.sort(Comparator.comparing(Stock::getQuantity, naturalOrder()));
    return result;
  }

  private List<Stock> fakeStockOut() {
    List<Stock> result = new ArrayList<>();
    result.add(new Stock(1, 0d, UnitType.G, initialDateTime, StockType.OUT));
    result.add(new Stock(2, 0d, UnitType.L, initialDateTime, StockType.OUT));
    result.add(new Stock(3, 0d, UnitType.U, initialDateTime, StockType.OUT));
    result.add(new Stock(4, 0d, UnitType.U, initialDateTime, StockType.OUT));
    result.add(
        new Stock(3, 10d, UnitType.U, LocalDateTime.of(2025, 2, 2, 10, 0, 0), StockType.OUT));
    result.add(
        new Stock(3, 10d, UnitType.U, LocalDateTime.of(2025, 2, 3, 15, 0, 0), StockType.OUT));
    result.add(
        new Stock(4, 20d, UnitType.U, LocalDateTime.of(2025, 2, 5, 16, 0, 0), StockType.OUT));
    result.sort(Comparator.comparing(Stock::getIngredientId, naturalOrder()));
    return result;
  }
}
