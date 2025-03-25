package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Status {
  private StatusType statusType;
  private LocalDateTime dateTime;

  public Status(StatusType statusType, LocalDateTime dateTime) {
    this.statusType = statusType;
    this.dateTime = dateTime;
  }

  @Override
  public String toString() {
    return "Status{" +
            "statusType=" + statusType +
            ", dateTime=" + dateTime +
            '}';
  }

  public StatusType getStatus() {
    return statusType;
  }

  public void setStatus(StatusType statusType) {
    this.statusType = statusType;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Status that = (Status) o;
    return statusType == that.statusType && Objects.equals(dateTime, that.dateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusType, dateTime);
  }
}
