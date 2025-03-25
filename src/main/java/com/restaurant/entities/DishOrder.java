package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.restaurant.entities.StatusType.*;
import static java.util.Comparator.naturalOrder;

public class DishOrder {
  private long id;
  private Dish dish;
  private long quantity;
  private String orderReference;
  private List<Status> statusList;

  public DishOrder(long id, Dish dish, String orderReference, long quantity, List<Status> statusList) {
    this.id = id;
    this.dish = dish;
    this.orderReference = orderReference;
    this.quantity = quantity;
    this.statusList = statusList;
  }
  public DishOrder(long id, Dish dish, String orderReference, long quantity) {
    this.id = id;
    this.dish = dish;
    this.orderReference = orderReference;
    this.quantity = quantity;
    this.statusList = new ArrayList<>();
    this.statusList.add(new Status(CREATED, LocalDateTime.of(2025,1,1,0,0,0)));
  }

  @Override
  public String toString() {
    return "DishOrder{" +
            "id=" + id +
            ", dish=" + dish +
            ", quantity=" + quantity +
            ", orderReference='" + orderReference + '\'' +
            ", statusList=" + statusList +
            '}';
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getOrderReference() {
    return orderReference;
  }

  public void setOrderReference(String orderReference) {
    this.orderReference = orderReference;
  }

  public Dish getDish() {
    return dish;
  }

  public void setDish(Dish dish) {
    this.dish = dish;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public List<Status> getStatusList() {
    return statusList;
  }

  public void setStatusList(List<Status> statusList) {
    this.statusList = statusList;
  }

  public boolean isAvailable(){
    return this.getDish().getAvailableQuantity() >= this.getQuantity();
  }
  public void updateStatus(){
    switch (this.getActualStatus().getStatus()){
     case IN_PROGRESS -> {
          Status status = new Status(DONE, LocalDateTime.of(2025,3,1,0,0,0));
          this.getStatusList().add(status);
        }
      case DONE -> {
        Status status = new Status(DELIVERED, LocalDateTime.of(2025,3,2,0,0,0));
        this.getStatusList().add(status);
      }
      default -> {
       throw new RuntimeException("cannot update status because it's delivered");
      }
    }
  }

  public long getTotalAmount(){
    return this.getDish().getDishPrice() * this.getQuantity();
  }
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DishOrder dishOrder = (DishOrder) o;
    return quantity == dishOrder.quantity
        && Objects.equals(dish, dishOrder.dish)
        && Objects.equals(statusList, dishOrder.statusList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dish, quantity, statusList);
  }

  public Status getActualStatus() {
    return this.getStatusList().stream()
        .max(Comparator.comparing(Status::getDateTime, naturalOrder()))
        .orElseThrow();
  }
}
