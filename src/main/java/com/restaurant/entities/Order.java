package com.restaurant.entities;

import com.restaurant.dao.OrderStatusDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.restaurant.entities.StatusType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode
@ToString
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
    OrderStatusDAO orderStatusDAO = new OrderStatusDAO();
    switch (this.getActualStatus().getStatusType()) {
      case CREATED -> {
        Status status = new Status(CONFIRMED, LocalDateTime.of(2025,2,1,0,0,0));
        this.statusList.add(status);
        orderStatusDAO.save(status,this.getReference());
      }
      case CONFIRMED -> {
        Status status = new Status(IN_PROGRESS, LocalDateTime.of(2025,3,1,0,0,0));
        this.statusList.add(status);
        orderStatusDAO.save(status,this.getReference());
      }
      case IN_PROGRESS -> {
        if (this.getDishOrderList().stream()
            .map(dishOrder -> dishOrder.getActualStatus().getStatusType())
            .filter(status -> !status.equals(DONE))
            .findAny()
            .isEmpty()) {
          Status status = new Status(DONE, LocalDateTime.of(2025,4,1,0,0,0));
          this.statusList.add(status);
          orderStatusDAO.save(status,this.getReference());
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

}
