package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Stock {
  private final long ingredientId;
  private final double quantity;
  private final UnitType unit;
  private final LocalDateTime lastModified;
  private final StockType type;

  public Stock(
      long ingredientId, double quantity, UnitType unit, LocalDateTime lastModified, StockType type) {
    this.ingredientId = ingredientId;
    this.quantity = quantity;
    this.unit = unit;
    this.lastModified = lastModified;
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Stock stock = (Stock) o;
    return ingredientId == stock.ingredientId
        && quantity == stock.quantity
        && unit == stock.unit
        && Objects.equals(lastModified, stock.lastModified)
        && type == stock.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingredientId, quantity, unit, lastModified, type);
  }

  @Override
  public String toString() {
    return "Stock{"
        + "ingredientId="
        + ingredientId
        + ", quantity="
        + quantity
        + ", unit="
        + unit
        + ", lastModified="
        + lastModified
        + ", type="
        + type
        + '}';
  }

  public long getIngredientId() {
    return ingredientId;
  }

  public double getQuantity() {
    return quantity;
  }

  public UnitType getUnit() {
    return unit;
  }

  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public StockType getType() {
    return type;
  }
}
