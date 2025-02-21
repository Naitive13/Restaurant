package com.restaurant.entities;

import java.util.Objects;

public class Sort extends Criteria{
    private Order order;

    public Sort(String column, Order order) {
        this.column = column;
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sort sort = (Sort) o;
        return Objects.equals(column, sort.column) && order == sort.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, order);
    }
}
