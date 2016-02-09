package com.epam.trainings.spring.core.dm.service.impl.strategies;

import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;

public class BirthdayDiscountStrategy implements DiscountService.DiscountStrategy {

    private double discountPercentage;

    @Override
    public double getDiscount(User user, Event event, LocalDateTime dateTime) {
        if (dateTime == null || event == null) {
            throw new IllegalArgumentException();
        }
        if (user == null) {
            return 0;
        }
        return user.getBirthDate().equals(dateTime.toLocalDate()) ? discountPercentage * event.getPrice() : 0;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

}
