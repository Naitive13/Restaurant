package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Ingredient {
  private final long ingredientId;
  private final String ingredientName;
  private final UnitType unit;
  private final List<Price> ingredientPrices;
  private final double quantity;
  private final LocalDateTime lastModified;

  public Ingredient(
      long ingredientId,
      String ingredientName,
      UnitType unit,
      List<Price> ingredientPrices,
      double quantity,
      LocalDateTime lastModified) {
    this.ingredientId = ingredientId;
    this.ingredientName = ingredientName;
    this.unit = unit;
    this.ingredientPrices = ingredientPrices;
    this.quantity = quantity;
    this.lastModified = lastModified;
  }

  @Override
  public String toString() {
    return "Ingredient{"
        + "ingredientId="
        + ingredientId
        + ", ingredientName='"
        + ingredientName
        + '\''
        + ", unit="
        + unit
        + ", ingredientPrices="
        + ingredientPrices
        + ", quantity="
        + quantity
        + ", lastModified="
        + lastModified
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ingredient that = (Ingredient) o;
    return ingredientId == that.ingredientId
        && Double.compare(quantity, that.quantity) == 0
        && Objects.equals(ingredientName, that.ingredientName)
        && unit == that.unit
        && Objects.equals(ingredientPrices, that.ingredientPrices)
        && Objects.equals(lastModified, that.lastModified);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        ingredientId, ingredientName, unit, ingredientPrices, quantity, lastModified);
  }

  public LocalDateTime getLastModified() {
    return lastModified;
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
