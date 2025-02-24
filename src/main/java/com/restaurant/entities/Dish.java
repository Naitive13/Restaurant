package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Dish {
    private final long dishId;
    private final String dishName;
    private final long dishPrice;
    private final List<Ingredient> ingredientList;

    public Dish(long dishId, String dishName, long dishPrice, List<Ingredient> ingredientList) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.ingredientList = ingredientList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return dishId == dish.dishId && dishPrice == dish.dishPrice && Objects.equals(dishName, dish.dishName) && Objects.equals(ingredientList, dish.ingredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId, dishName, dishPrice, ingredientList);
    }

    public long getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public long getDishPrice() {
        return dishPrice;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "dishId=" + dishId +
                ", dishName='" + dishName + '\'' +
                ", dishPrice=" + dishPrice +
                ", ingredientList=" + ingredientList +
                '}';
    }

    // my stuff
    public double getProductionCost(LocalDateTime dateTime) {
//       float result = 0;
//       for (Ingredient ingredient:this.getIngredientList()){
//           result+= ingredient.getIngredientPrices()*ingredient.getQuantity();
//       }
//        return result;
        return this.getIngredientList().stream()
                .map((ingredient -> {
                    Price price = ingredient.getIngredientPrices().stream().filter(price1 -> price1.getDate().equals(dateTime)).findFirst().get();
                    return price.getValue() * ingredient.getQuantity();
                }))
                .reduce((double) (0), Double::sum);
    }
}
