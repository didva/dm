package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Counter;

public interface DiscountCounterDao {

    void increase(String discountName, long userId);

    Counter<Long> findByDiscountName(String discountName);

    Counter<String> findByUserId(long userId);

}
