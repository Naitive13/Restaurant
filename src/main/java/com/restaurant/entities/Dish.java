package com.restaurant.entities;

import lombok.*;

import static java.util.Comparator.naturalOrder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class Dish {
  private final long dishId;
  private final String dishName;
  private final long dishPrice;
  private final List<Ingredient> ingredientList;

  // my stuff
  public double getProductionCost(LocalDateTime dateTime) {
    return this.getIngredientList().stream()
        .map(
            (ingredient -> {
              Price price =
                  ingredient.getIngredientPrices().stream()
                      .filter(price1 -> price1.getDate().equals(dateTime))
                      .findFirst()
                      .orElseThrow();
              return price.getValue() * ingredient.getQuantity();
            }))
        .reduce((double) (0), Double::sum);
  }

  public double getProductionCost() {
    return this.getIngredientList().stream()
        .map(
            (ingredient -> {
              Price price =
                  ingredient.getIngredientPrices().stream()
                      .sorted(Comparator.comparing(Price::getDate, naturalOrder()).reversed())
                      .findFirst()
                      .orElseThrow();
              return price.getValue() * ingredient.getQuantity();
            }))
        .reduce(0.0, Double::sum);
  }

  public double getGrossMargin() {
    return this.dishPrice - this.getProductionCost();
  }

  public double getGrossMargin(LocalDateTime dateTime) {
    return this.dishPrice - this.getProductionCost(dateTime);
  }

  public long getAvailableQuantity() {
    return this.getIngredientList().stream()
        .map(
            ingredient -> {
              return Math.round(ingredient.getAvailableQuantity() / ingredient.getQuantity());
            })
        .min(Long::compare)
        .orElseThrow();
  }

  public long getAvailableQuantity(LocalDateTime date) {
    return this.getIngredientList().stream()
        .map(
            ingredient -> {
              return Math.round(ingredient.getAvailableQuantity(date) / ingredient.getQuantity());
            })
        .min(Long::compare)
        .orElseThrow();
  }
}
