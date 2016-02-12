package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.model.Counter;

import java.util.HashMap;
import java.util.Map;

public class DiscountCounterDaoInMemoryImpl implements DiscountCounterDao {

    private Map<String, Integer> statisticsByDiscount = new HashMap<>();
    private Map<Long, Integer> statisticsByUser = new HashMap<>();

    @Override
    public void increase(String discountName, long userId) {
        statisticsByDiscount.compute(discountName, (dName, counter) -> counter == null ? 1 : counter + 1);
        statisticsByUser.compute(userId, (uId, counter) -> counter == null ? 1 : counter + 1);
    }

    @Override
    public Counter<String> findByDiscountName(String discountName) {
        return new Counter<>(discountName, statisticsByUser.getOrDefault(discountName, 0));
    }

    @Override
    public Counter<Long> findByUserId(long userId) {
        return new Counter<>(userId, statisticsByUser.getOrDefault(userId, 0));
    }
}
