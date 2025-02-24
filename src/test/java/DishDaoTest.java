import com.restaurant.dao.DishDAO;
import com.restaurant.entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DishDaoTest {
    DishDAO subject = new DishDAO();
    LocalDateTime fakeDateTime = LocalDateTime.of(2025,1,1,0,0,0);
    Price sausagePrice = new Price(1,20,fakeDateTime);
    Price oilPrice = new Price(2,10_000,fakeDateTime);
    Price eggPrice = new Price(3,1_000,fakeDateTime);
    Price breadPrice = new Price(4,1_000,fakeDateTime);

    @Test
    void get_the_dish_and_calculate_the_production_cost (){
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(new Ingredient(1,"sausage", UnitType.G,List.of(sausagePrice),100));
        ingredientList.add(new Ingredient(2,"oil", UnitType.L,List.of(oilPrice),0.15d));
        ingredientList.add(new Ingredient(3,"egg", UnitType.U,List.of(eggPrice),1));
        ingredientList.add(new Ingredient(4,"bread", UnitType.U,List.of(breadPrice),1));
        Dish expectedDish = new Dish(1,"hotdog",15_000,ingredientList);
        double expectedProductionCost = 5500;

        Dish actualDish = subject.get(new ArrayList<Criteria>(),1,10).getFirst();
        double actualProductionCost = actualDish.getProductionCost(fakeDateTime);

        assertEquals(expectedDish,actualDish);
        assertEquals(expectedProductionCost,actualProductionCost);
    }


}
