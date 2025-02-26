package com.restaurant.dao;

import com.restaurant.entities.Criteria;

import java.util.List;

public interface CrudDAO <E>{
    List<E> get(List<Criteria> criteriaList, int pageIndex, int pageSize);
    void save(E element);
}
