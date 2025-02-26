package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class DateTimeInterval {
  private final LocalDateTime date1;
    private final LocalDateTime date2;

  public DateTimeInterval(LocalDateTime date1, LocalDateTime date2) {
    this.date1 = date1;
    this.date2 = date2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DateTimeInterval that = (DateTimeInterval) o;
    return Objects.equals(date1, that.date1) && Objects.equals(date2, that.date2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date1, date2);
  }

  public LocalDateTime getDate1() {
    return date1;
  }

  public LocalDateTime getDate2() {
    return date2;
  }
}
