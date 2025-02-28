import static org.junit.jupiter.api.Assertions.assertEquals;

import com.restaurant.dao.IngredientDAO;
import com.restaurant.entities.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class IngredientTest {
  private final IngredientDAO subject = new IngredientDAO();

  @Test
  void get_all_ingredients() {
    List<Ingredient> expected = ingredientList();

    List<Ingredient> actual = subject.get(new ArrayList<>(), 1, 10);

    assertEquals(expected, actual, "get all ingredient");
  }

  @Test
  void get_available_quantity_for_all_ingredients_before_usage() {
    double expectedForSausage = 10_000d;
    double expectedForOil = 20d;
    double expectedForEgg = 100d;
    double expectedForBread = 50d;

    LocalDateTime dateTimeLimit = LocalDateTime.of(2025, 2, 1, 8, 0, 0);

    List<Ingredient> ingredientList = subject.get(new ArrayList<Criteria>(), 1, 10);
    double actualForSausage = ingredientList.removeFirst().getAvailableQuantity(dateTimeLimit);
    double actualForOil = ingredientList.removeFirst().getAvailableQuantity(dateTimeLimit);
    double actualForEgg = ingredientList.removeFirst().getAvailableQuantity(dateTimeLimit);
    double actualForBread = ingredientList.removeFirst().getAvailableQuantity(dateTimeLimit);

    assertEquals(expectedForSausage, actualForSausage, "available quantity for Sausage");
    assertEquals(expectedForOil, actualForOil, "available quantity for Oil");
    assertEquals(expectedForEgg, actualForEgg, "available quantity for Egg");
    assertEquals(expectedForBread, actualForBread, "available quantity for Bread");
  }

  @Test
  void get_available_quantity_for_all_ingredients_after_usage() {
    double expectedForSausage = 10_000d;
    double expectedForOil = 20d;
    double expectedForEgg = 80d;
    double expectedForBread = 30d;

    List<Ingredient> ingredientList = subject.get(new ArrayList<Criteria>(), 1, 10);
    double actualForSausage = ingredientList.removeFirst().getAvailableQuantity();
    double actualForOil = ingredientList.removeFirst().getAvailableQuantity();
    double actualForEgg = ingredientList.removeFirst().getAvailableQuantity();
    double actualForBread = ingredientList.removeFirst().getAvailableQuantity();

    assertEquals(expectedForSausage, actualForSausage, "available quantity for Sausage");
    assertEquals(expectedForOil, actualForOil, "available quantity for Oil");
    assertEquals(expectedForEgg, actualForEgg, "available quantity for Egg");
    assertEquals(expectedForBread, actualForBread, "available quantity for Bread");
  }

  @Test
  void save_new_ingredients() {
    List<Ingredient> expectedIngredients = newIngredientList();

    subject.save(salt());
    subject.save(rice());
    List<Criteria> criteria = new ArrayList<>();
    criteria.add(new Sort("ingredient_id", Order.DESC));
    List<Ingredient> actualIngredients = subject.get(criteria, 1, 2);

    assertEquals(expectedIngredients, actualIngredients);
  }

  private List<Ingredient> ingredientList() {
    List<Ingredient> result = new ArrayList<>();
    LocalDateTime initialDateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    result.add(
        new Ingredient(
            1,
            "sausage",
            UnitType.G,
            List.of(new Price(1, 20, initialDateTime)),
            100d,
            initialDateTime));
    result.add(
        new Ingredient(
            2,
            "oil",
            UnitType.L,
            List.of(new Price(2, 10_000, initialDateTime)),
            0.15d,
            initialDateTime));
    result.add(
        new Ingredient(
            3,
            "egg",
            UnitType.U,
            List.of(new Price(3, 1_000, initialDateTime)),
            1d,
            initialDateTime));
    result.add(
        new Ingredient(
            4,
            "bread",
            UnitType.U,
            List.of(new Price(4, 1_000, initialDateTime)),
            1d,
            initialDateTime));
    return result;
  }

  private Ingredient salt() {
    return new Ingredient(
        5,
        "salt",
        UnitType.G,
        List.of(new Price(5, 2.5d, LocalDateTime.of(2025, 3, 1, 0, 0, 0))),
        0d,
        LocalDateTime.of(2025, 3, 1, 0, 0, 0));
  }

  private Ingredient rice() {
    return new Ingredient(
        6,
        "rice",
        UnitType.L,
        List.of(new Price(6, 3.5d, LocalDateTime.of(2025, 3, 1, 0, 0, 0))),
        0d,
        LocalDateTime.of(2025, 3, 1, 0, 0, 0));
  }

  private List<Ingredient> newIngredientList() {
    List<Ingredient> result = new ArrayList<>();
    result.add(rice());
    result.add(salt());
    return result;
  }
}
