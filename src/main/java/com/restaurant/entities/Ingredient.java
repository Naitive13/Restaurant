package com.restaurant.entities;

import com.restaurant.dao.StockInDAO;
import com.restaurant.dao.StockOutDAO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode
@AllArgsConstructor
@Data
@ToString
public class Ingredient {
  private long ingredientId;
  private String ingredientName;
  private UnitType unit;
  private List<Price> ingredientPrices;
  private double quantity;
  private LocalDateTime lastModified;

  // my stuff
  public double getAvailableQuantity() {
    double stockIn = new StockInDAO().getTotalForIngredient(this.ingredientId);
    double stockOut = new StockOutDAO().getTotalForIngredient(this.ingredientId);
    return stockIn - stockOut;
  }

  public double getAvailableQuantity(LocalDateTime dateTime) {
    double stockIn = new StockInDAO().getTotalForIngredient(this.ingredientId, dateTime);
    double stockOut = new StockOutDAO().getTotalForIngredient(this.ingredientId, dateTime);
    return stockIn - stockOut;
  }
}
