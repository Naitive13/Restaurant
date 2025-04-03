package com.restaurant.entities;

import com.restaurant.dao.DishOrderStatusDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.restaurant.entities.StatusType.*;
import static java.util.Comparator.naturalOrder;

@EqualsAndHashCode
@ToString
@Data
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

  public boolean isAvailable(){
    return this.getDish().getAvailableQuantity() >= this.getQuantity();
  }
  public void updateStatus(){
    DishOrderStatusDAO dishOrderStatusDAO = new DishOrderStatusDAO();
    switch (this.getActualStatus().getStatusType()){
      case CREATED -> {
        Status status = new Status(IN_PROGRESS, LocalDateTime.of(2025,3,1,0,0,0));
        this.getStatusList().add(status);
        dishOrderStatusDAO.save(status,this.getId());
      }
     case IN_PROGRESS -> {
          Status status = new Status(DONE, LocalDateTime.of(2025,3,2,0,0,0));
          this.getStatusList().add(status);
          dishOrderStatusDAO.save(status,this.getId());
        }
      case DONE -> {
        Status status = new Status(DELIVERED, LocalDateTime.of(2025,3,3,0,0,0));
        this.getStatusList().add(status);
        dishOrderStatusDAO.save(status,this.getId());
      }
      default -> {
       throw new RuntimeException("cannot update status because it's delivered");
      }
    }
  }

  public long getTotalAmount(){
    return this.getDish().getDishPrice() * this.getQuantity();
  }


  public Status getActualStatus() {
    return this.getStatusList().stream()
        .max(Comparator.comparing(Status::getDateTime, naturalOrder()))
        .orElseThrow();
  }
}
