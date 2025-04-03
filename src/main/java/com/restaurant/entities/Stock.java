package com.restaurant.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Stock {
  private long ingredientId;
  private double quantity;
  private UnitType unit;
  private LocalDateTime lastModified;
  private StockType type;

}
