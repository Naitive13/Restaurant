package com.restaurant.entities;

import java.util.Objects;

public class Sort extends Criteria {
  private final SortOrder sortOrder;

  public Sort(String column, SortOrder sortOrder) {
    this.column = column;
    this.sortOrder = sortOrder;
  }

  public SortOrder getOrder() {
    return sortOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sort sort = (Sort) o;
    return Objects.equals(column, sort.column) && sortOrder == sort.sortOrder;
  }

  @Override
  public int hashCode() {
    return Objects.hash(column, sortOrder);
  }
}
