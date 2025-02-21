import com.restaurant.dao.DishDAO;
import com.restaurant.entities.Criteria;
import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;
import com.restaurant.entities.UnitType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DishDaoTest {
    DishDAO subject = new DishDAO();

    @Test
    void get_the_dish_and_calculate_the_production_cost (){
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(new Ingredient(1,"sausage", UnitType.G,20,100));
        ingredientList.add(new Ingredient(2,"oil", UnitType.L,10_000,0.15f));
        ingredientList.add(new Ingredient(3,"egg", UnitType.U,1000,1));
        ingredientList.add(new Ingredient(4,"bread", UnitType.U,1000,1));
        Dish expectedDish = new Dish(1,"hotdog",15_000,ingredientList);
        double expectedProductionCost = 5500;

        Dish actualDish = subject.getAll(new ArrayList<Criteria>(),1,10).getFirst();
        double actualProductionCost = actualDish.getProductionCost();

        assertEquals(expectedDish,actualDish);
        assertEquals(expectedProductionCost,actualProductionCost);

    }


}
