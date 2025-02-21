package com.restaurant.entities;

import java.util.Objects;

public class Filter extends Criteria{
    private Object value;

    public Filter(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(column, filter.column) && Objects.equals(value, filter.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, value);
    }

    public Object getValue() {
        return value;
    }
}
