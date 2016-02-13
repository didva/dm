package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Counter;

import java.util.List;

public interface DiscountCounterDao {

    void increase(String discountName, long userId);

    List<Counter<String>> findAll();

    List<Counter<String>> findByUserId(long userId);

}
