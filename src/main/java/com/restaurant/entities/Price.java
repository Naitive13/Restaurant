package com.restaurant.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Price {
  private long ingredientId;
  private double value;
  private LocalDateTime date;
}
