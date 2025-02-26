import static org.junit.jupiter.api.Assertions.assertEquals;

import com.restaurant.dao.DishDAO;
import com.restaurant.entities.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DishDaoTest {
  DishDAO subject = new DishDAO();
  LocalDateTime fakeDateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

  private List<Ingredient> ingredientList() {
    Price sausagePrice = new Price(1, 20, fakeDateTime);
    Price oilPrice = new Price(2, 10_000, fakeDateTime);
    Price eggPrice = new Price(3, 1_000, fakeDateTime);
    Price breadPrice = new Price(4, 1_000, fakeDateTime);
    List<Ingredient> ingredientList = new ArrayList<>();
    ingredientList.add(
        new Ingredient(1, "sausage", UnitType.G, List.of(sausagePrice), 100, fakeDateTime));
    ingredientList.add(
        new Ingredient(2, "oil", UnitType.L, List.of(oilPrice), 0.15d, fakeDateTime));
    ingredientList.add(new Ingredient(3, "egg", UnitType.U, List.of(eggPrice), 1, fakeDateTime));
    ingredientList.add(
        new Ingredient(4, "bread", UnitType.U, List.of(breadPrice), 1, fakeDateTime));
    return ingredientList;
  }

  @Test
  void get_the_dish() {
    Dish expectedDish = new Dish(1, "hotdog", 15_000, ingredientList());

    Dish actualDish = subject.get(new ArrayList<Criteria>(), 1, 10).getFirst();

    assertEquals(expectedDish, actualDish);
  }

  @Test
  void get_the_correct_production_cost() {
    double expectedProductionCost = 5500;

    Dish dish = subject.get(new ArrayList<Criteria>(), 1, 10).getFirst();
    double actualProductionCost = dish.getProductionCost();

    assertEquals(expectedProductionCost, actualProductionCost);
  }

  @Test
  void get_the_correct_gross_margin() {
    double expectedGrossMargin = 15_000 - 5_500;

    Dish dish = subject.get(new ArrayList<Criteria>(), 1, 10).getFirst();
    double actualGrossMargin = dish.getGrossMargin();

    assertEquals(expectedGrossMargin, actualGrossMargin);
  }
}
