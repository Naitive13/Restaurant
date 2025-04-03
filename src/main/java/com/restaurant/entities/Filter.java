package com.restaurant.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class Filter extends Criteria {
  private  Object value;

  public Filter(String column, Object value) {
    super(column);
    this.value = value;
  }
}
