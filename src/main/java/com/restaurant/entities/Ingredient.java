package com.restaurant.entities;

import java.util.List;
import java.util.Objects;

public class Ingredient {
    private long ingredientId;
    private String ingredientName;
    private UnitType unit;
    private List<Price> ingredientPrices;
    private double quantity;

    public Ingredient(long ingredientId, String ingredientName, UnitType unit, List<Price> ingredientPrices, double quantity) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.ingredientPrices = ingredientPrices;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return ingredientId == that.ingredientId && Double.compare(quantity, that.quantity) == 0 && Objects.equals(ingredientName, that.ingredientName) && unit == that.unit && Objects.equals(ingredientPrices, that.ingredientPrices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, ingredientName, unit, ingredientPrices, quantity);
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientId=" + ingredientId +
                ", ingredientName='" + ingredientName + '\'' +
                ", unit=" + unit +
                ", ingredientPrices=" + ingredientPrices +
                ", quantity=" + quantity +
                '}';
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public UnitType getUnit() {
        return unit;
    }

    public List<Price> getIngredientPrices() {
        return ingredientPrices;
    }

    public double getQuantity() {
        return quantity;
    }
}
