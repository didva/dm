package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.model.Counter;

import java.util.*;

public class DiscountCounterDaoInMemoryImpl implements DiscountCounterDao {

    private Map<String, Integer> statisticsByDiscount = new HashMap<>();
    private Map<Long, Map<String, Integer>> statisticsByUser = new HashMap<>();

    @Override
    public void increase(String discountName, long userId) {
        statisticsByDiscount.compute(discountName, (dName, counter) -> counter == null ? 1 : counter + 1);
        Map<String, Integer> userInfo = statisticsByUser.get(userId);
        if (userInfo == null) {
            userInfo = new HashMap<>();
            statisticsByUser.put(userId, userInfo);
        }
        userInfo.compute(discountName, (dName, counter) -> counter == null ? 1 : counter + 1);
    }

    @Override
    public List<Counter<String>> findAll() {
        List<Counter<String>> statistics = new ArrayList<>();
        statisticsByDiscount.entrySet().stream().forEach(e -> statistics.add(new Counter<>(e.getKey(), e.getValue())));
        return statistics;
    }

    @Override
    public List<Counter<String>> findByUserId(long userId) {
        Map<String, Integer> userInfo = statisticsByUser.get(userId);
        if (userInfo == null) {
            return Collections.emptyList();
        }
        List<Counter<String>> statistics = new ArrayList<>();
        userInfo.entrySet().stream().forEach(e -> statistics.add(new Counter<>(e.getKey(), e.getValue())));
        return statistics;
    }
}
