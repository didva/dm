package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.model.Event
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.model.User
import com.epam.trainings.spring.core.dm.service.DiscountService
import org.junit.Before
import org.junit.Test

import java.time.LocalDate
import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.createEvent
import static com.epam.trainings.spring.core.dm.Utils.createUser
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class TestDiscountServiceImpl {

    DiscountServiceImpl discountService;
    User user
    Event event

    @Before
    void init() {
        discountService = DiscountServiceImpl.newInstance()
        user = createUser 1, "user1", "email", LocalDate.now()
        event = createEvent "name", 10, Rating.HIGH
    }

    @Test
    void testGetDiscount() {
        double maxDiscount = 50D, minDiscount = 10D
        LocalDateTime dateTime = LocalDateTime.now()
        def strategy1 = mock(DiscountService.DiscountStrategy.class),
            strategy2 = mock(DiscountService.DiscountStrategy.class)
        when(strategy1.getDiscount(user, event, dateTime)).thenReturn maxDiscount
        when(strategy2.getDiscount(user, event, dateTime)).thenReturn minDiscount
        discountService.strategies = [strategy1, strategy2]

        assert maxDiscount == discountService.getDiscount(user, event, dateTime)
    }

    @Test
    void testGetDiscountWithoutStrategies() {
        assert 0D == discountService.getDiscount(user, event, LocalDateTime.now())
    }

    @Test
    void testGetDiscountWithoutEmptyStrategies() {
        discountService.strategies = []
        assert 0D == discountService.getDiscount(user, event, LocalDateTime.now())
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetDiscountIncorrectEvent() {
        discountService.getDiscount(user, null, LocalDateTime.now())
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetDiscountIncorrectDateTime() {
        discountService.getDiscount(user, event, null)
    }

}
