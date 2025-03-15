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
  private final StockInDAO subjectStockIn = new StockInDAO();
  private final StockOutDAO subjectStockOut = new StockOutDAO();
  private final LocalDateTime initialDateTime = LocalDateTime.of(2025, 1, 1, 1, 0, 0);

  @Test
  void get_stock_movement_in() {
    List<Stock> expected = fakeStockIn();

    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("ingredient_id", SortOrder.ASC));
    List<Stock> actual = subjectStockIn.get(criteria, 1, 8);

    assertEquals(expected, actual);
  }

  @Test
  void get_stock_movement_out() {
    List<Stock> expected = fakeStockOut();

    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("ingredient_id", SortOrder.ASC));
    List<Stock> actual = subjectStockOut.get(criteria, 1, 7);

    assertEquals(expected, actual);
  }

  @Test
  void add_stock_movement_in() {
    List<Stock> expectedResult = newStockInList();

    subjectStockIn.save(riceStockIn());
    subjectStockIn.save(saltStockIn());
    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("last_modified", SortOrder.DESC));
    List<Stock> actualResult = subjectStockIn.get(criteria, 1, 2);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  void add_stock_movement_out() {
    List<Stock> expectedResult = newStockOutList();

    subjectStockOut.save(riceStockOut());
    subjectStockOut.save(saltStockOut());

    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("last_modified", SortOrder.DESC));
    List<Stock> actualResult = subjectStockOut.get(criteria, 1, 2);

    assertEquals(expectedResult, actualResult);
  }

  private List<Stock> fakeStockIn() {
    List<Stock> result = new ArrayList<>();
    final LocalDateTime restockDateTime = LocalDateTime.of(2025, 2, 1, 8, 0, 0);
    result.add(new Stock(1, 0d, UnitType.G, initialDateTime, StockType.IN));
    result.add(new Stock(1, 10_000d, UnitType.G, restockDateTime, StockType.IN));
    result.add(new Stock(2, 0d, UnitType.L, initialDateTime, StockType.IN));
    result.add(new Stock(2, 20d, UnitType.L, restockDateTime, StockType.IN));
    result.add(new Stock(3, 0d, UnitType.U, initialDateTime, StockType.IN));
    result.add(new Stock(3, 100d, UnitType.U, restockDateTime, StockType.IN));
    result.add(new Stock(4, 0d, UnitType.U, initialDateTime, StockType.IN));
    result.add(new Stock(4, 50d, UnitType.U, restockDateTime, StockType.IN));
    result.sort(Comparator.comparing(Stock::getIngredientId,naturalOrder()));
    return result;
  }

  private List<Stock> fakeStockOut() {
    List<Stock> result = new ArrayList<>();
    result.add(new Stock(1, 0d, UnitType.G, initialDateTime, StockType.OUT));
    result.add(new Stock(2, 0d, UnitType.L, initialDateTime, StockType.OUT));
    result.add(new Stock(3, 0d, UnitType.U, initialDateTime, StockType.OUT));
    result.add(
        new Stock(3, 10d, UnitType.U, LocalDateTime.of(2025, 2, 2, 10, 0, 0), StockType.OUT));
    result.add(
        new Stock(3, 10d, UnitType.U, LocalDateTime.of(2025, 2, 3, 15, 0, 0), StockType.OUT));
    result.add(new Stock(4, 0d, UnitType.U, initialDateTime, StockType.OUT));
    result.add(
        new Stock(4, 20d, UnitType.U, LocalDateTime.of(2025, 2, 5, 16, 0, 0), StockType.OUT));

    result.sort(Comparator.comparing(Stock::getIngredientId, naturalOrder()));
    return result;
  }

  private Stock riceStockIn() {
    return new Stock(6, 3_000d, UnitType.G, LocalDateTime.of(2025, 2, 28, 15, 0, 0), StockType.IN);
  }

  private Stock saltStockIn() {
    return new Stock(5, 1_000d, UnitType.G, LocalDateTime.of(2025, 2, 28, 15, 0, 0), StockType.IN);
  }

  private Stock saltStockOut() {
    return new Stock(5, 500d, UnitType.G, LocalDateTime.of(2025, 3, 1, 10, 0, 0), StockType.OUT);
  }

  private Stock riceStockOut() {
    return new Stock(6, 1_000d, UnitType.G, LocalDateTime.of(2025, 3, 1, 10, 0, 0), StockType.OUT);
  }

  private List<Stock> newStockInList() {
    List<Stock> result = new ArrayList<>();
    result.add(riceStockIn());
    result.add(saltStockIn());
    return result;
  }

  private List<Stock> newStockOutList() {
    List<Stock> result = new ArrayList<>();
    result.add(riceStockOut());
    result.add(saltStockOut());
    return result;
  }
}
