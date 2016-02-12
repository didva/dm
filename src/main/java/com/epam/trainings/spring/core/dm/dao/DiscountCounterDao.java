package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Counter;

public interface DiscountCounterDao {

    void increase(String discountName, long userId);

    Counter<String> findByDiscountName(String discountName);

    Counter<Long> findByUserId(long userId);

}
