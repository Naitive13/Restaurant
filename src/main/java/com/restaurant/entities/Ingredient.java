package com.restaurant.entities;

import java.util.Objects;

public class Ingredient {
    private int ingredientId;
    private String ingredientName;
    private UnitType unit;
    private int ingredientPrice;
    private float quantity;

    public Ingredient(int ingredientId, String ingredientName, UnitType unit, int ingredientPrice, float quantity) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.ingredientPrice = ingredientPrice;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return ingredientId == that.ingredientId && ingredientPrice == that.ingredientPrice && Double.compare(quantity, that.quantity) == 0 && Objects.equals(ingredientName, that.ingredientName) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, ingredientName, unit, ingredientPrice, quantity);
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientId=" + ingredientId +
                ", ingredientName='" + ingredientName + '\'' +
                ", unit=" + unit +
                ", ingredientPrice=" + ingredientPrice +
                ", quantity=" + quantity +
                '}';
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public UnitType getUnit() {
        return unit;
    }

    public int getIngredientPrice() {
        return ingredientPrice;
    }

    public float getQuantity() {
        return quantity;
    }
}
