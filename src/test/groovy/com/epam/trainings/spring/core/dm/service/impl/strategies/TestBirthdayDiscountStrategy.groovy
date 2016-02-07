package com.epam.trainings.spring.core.dm.service.impl.strategies

import com.epam.trainings.spring.core.dm.model.Rating
import org.junit.Before
import org.junit.Test

import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.createEvent
import static com.epam.trainings.spring.core.dm.Utils.createUser

class TestBirthdayDiscountStrategy {

    BirthdayDiscountStrategy birthdayDiscountStrategy
    double discountPercentage = 0.05

    @Before
    void init() {
        birthdayDiscountStrategy = BirthdayDiscountStrategy.newInstance()
        birthdayDiscountStrategy.discountPercentage = discountPercentage
    }

    @Test
    void testGetDiscountAtBirthday() {
        def price = 100, discount = 5D, dateTime = LocalDateTime.now(),
            user = createUser(1, "name", "email", dateTime.toLocalDate()),
            event = createEvent("eName", price, Rating.HIGH)
        assert discount == birthdayDiscountStrategy.getDiscount(user, event, dateTime)
    }

    @Test
    void testGetDiscountNotAtBirthday() {
        def price = 100, discount = 0D, dateTime = LocalDateTime.now(),
            user = createUser(1, "name", "email", dateTime.toLocalDate().plusDays(1)),
            event = createEvent("eName", price, Rating.HIGH)

        assert discount == birthdayDiscountStrategy.getDiscount(user, event, dateTime)
    }

    @Test
    void testGetDiscountForAnonymous() {
        def price = 100, discount = 0D, dateTime = LocalDateTime.now(),
            event = createEvent("eName", price, Rating.HIGH)

        assert discount == birthdayDiscountStrategy.getDiscount(null, event, dateTime)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetDiscountIncorrectEvent() {
        birthdayDiscountStrategy.getDiscount null, null, LocalDateTime.now()
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetDiscountIncorrectDateTime() {
        birthdayDiscountStrategy.getDiscount(null, createEvent("eName", 100, Rating.HIGH), null)
    }

}
