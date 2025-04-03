package com.restaurant.entities;

import lombok.*;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class Sort extends Criteria {
  private  SortOrder sortOrder;
  public Sort(String column, SortOrder sortOrder) {
    super(column);
    this.sortOrder = sortOrder;
  }
}
