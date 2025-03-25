package com.restaurant.entities;

import static com.restaurant.entities.StatusType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Order {
  private String reference;
  private LocalDateTime creationDate;
  private List<DishOrder> dishOrderList;
  private List<Status> statusList;

  public Order(String reference, LocalDateTime creationDate, List<DishOrder> dishOrderList, List<Status> statusList) {
    this.reference = reference;
    this.creationDate = creationDate;
    this.dishOrderList = dishOrderList;
    this.statusList = statusList;
  }

  public Order(
      String reference,
      LocalDateTime creationDate,
      List<DishOrder> dishOrderList
      ) {
    this.reference = reference;
    this.creationDate = creationDate;
    this.dishOrderList = dishOrderList;
    this.statusList = new ArrayList<>();
    this.statusList.add(new Status(CREATED, LocalDateTime.of(2025,1,1,0,0,0)));
  }

  @Override
  public String toString() {
    return "Order{" +
            "reference='" + reference + '\'' +
            ", creationDate=" + creationDate +
            ", dishOrderList=" + dishOrderList +
            ", statusList=" + statusList +
            '}';
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public List<DishOrder> getDishOrderList() {
    return dishOrderList;
  }

  public void setDishOrderList(List<DishOrder> dishOrderList) {
    this.dishOrderList = dishOrderList;
  }

  public List<Status> getStatusList() {
    return statusList;
  }

  public void setStatusList(List<Status> statusList) {
    this.statusList = statusList;
  }

  public void addDishOrder(List<DishOrder> target) {
    if(target.stream().allMatch(DishOrder::isAvailable)){
      this.setDishOrderList(target);
    } else {
      throw new RuntimeException("There isn't enough ingredient for this dish");
    }
  }

  public Status getActualStatus() {
    return this.getStatusList().stream()
        .max(Comparator.comparing(Status::getDateTime))
        .orElseThrow();
  }

  public void updateStatus() {
    switch (this.getActualStatus().getStatus()) {
      case CREATED -> {
        Status status = new Status(CONFIRMED, LocalDateTime.of(2025,1,1,0,0,0));
        this.statusList.add(status);
      }
      case CONFIRMED -> {
        Status status = new Status(IN_PROGRESS, LocalDateTime.of(2025,2,1,0,0,0));
        this.statusList.add(status);
      }
      case IN_PROGRESS -> {
        if (this.getDishOrderList().stream()
            .map(dishOrder -> dishOrder.getActualStatus().getStatus())
            .filter(status -> !status.equals(DONE))
            .findAny()
            .isEmpty()) {
          Status status = new Status(DONE, LocalDateTime.of(2025,1,1,0,0,0));
          this.statusList.add(status);
        } else {
          throw new RuntimeException("Some dishes aren't done yet");
        }
      }
      default -> {
        throw new RuntimeException("cannot update status because it's done");
      }
    }
  }

  public long getTotalAmount() {
    return this.getDishOrderList().stream().map(DishOrder::getTotalAmount).reduce(0L, Long::sum);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(reference, order.reference)
        && Objects.equals(creationDate, order.creationDate)
        && Objects.equals(dishOrderList, order.dishOrderList)
        && Objects.equals(statusList, order.statusList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference, creationDate, dishOrderList, statusList);
  }
}
