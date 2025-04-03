package com.restaurant.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@EqualsAndHashCode
@ToString
public class DateTimeInterval {
  private final LocalDateTime date1;
    private final LocalDateTime date2;

  public DateTimeInterval(LocalDateTime date1, LocalDateTime date2) {
    this.date1 = date1;
    this.date2 = date2;
  }

}
