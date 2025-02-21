package com.restaurant.entities;

import java.util.List;
import java.util.Objects;

public class Dish {
    private int dishId;
    private String dishName;
    private int dishPrice;
    private List<Ingredient> ingredientList;

    public Dish(int dishId, String dishName, int dishPrice, List<Ingredient> ingredientList) {
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

    public int getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public int getDishPrice() {
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
    public double getProductionCost(){
//       float result = 0;
//       for (Ingredient ingredient:this.getIngredientList()){
//           result+= ingredient.getIngredientPrice()*ingredient.getQuantity();
//       }
//        return result;
        return this.getIngredientList().stream()
                .map((ingredient -> ingredient.getQuantity()*ingredient.getIngredientPrice()))
                .reduce(0f, Float::sum);
    }
}
