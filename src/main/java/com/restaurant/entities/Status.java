package com.restaurant.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Status {
  private StatusType statusType;
  private LocalDateTime dateTime;
}
