package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Price {
  private final long ingredientId;
  private final long value;
  private final LocalDateTime date;

  public Price(long ingredientId, long value, LocalDateTime date) {
    this.ingredientId = ingredientId;
    this.value = value;
    this.date = date;
  }

  @Override
  public String toString() {
    return "Price{" + "ingredientId=" + ingredientId + ", value=" + value + ", date=" + date + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return ingredientId == price.ingredientId
        && value == price.value
        && Objects.equals(date, price.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingredientId, value, date);
  }

  public long getValue() {
    return value;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public long getIngredientId() {
    return ingredientId;
  }
}
