package com.restaurant.entities;

import lombok.Data;

@Data
public abstract class Criteria {
  protected String column;

  public Criteria(String column) {
    this.column = column;
  }

}
