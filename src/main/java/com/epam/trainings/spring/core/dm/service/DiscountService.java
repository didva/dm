package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;

import java.time.LocalDateTime;

public interface DiscountService {

    double checkDiscount(User user, Event event, LocalDateTime dateTime);

    double getDiscount(User user, Event event, LocalDateTime dateTime);

    interface DiscountStrategy {

        double getDiscount(User user, Event event, LocalDateTime dateTime);

        double checkDiscount(User user, Event event, LocalDateTime dateTime);

    }

}
