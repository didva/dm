package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;
import java.util.List;

public class DiscountServiceImpl implements DiscountService {

    private List<DiscountStrategy> strategies;

    @Override
    public double checkDiscount(User user, Event event, LocalDateTime dateTime) {
        if (event == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        if (strategies == null) {
            return 0;
        }
        return strategies.stream().mapToDouble(strategy -> strategy.checkDiscount(user, event, dateTime)).max().orElse(0);
    }

    @Override
    public double getDiscount(User user, Event event, LocalDateTime dateTime) {
        if (event == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        if (strategies == null) {
            return 0;
        }
        return strategies.stream().mapToDouble(strategy -> strategy.getDiscount(user, event, dateTime)).max().orElse(0);
    }

    public void setStrategies(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }
}
