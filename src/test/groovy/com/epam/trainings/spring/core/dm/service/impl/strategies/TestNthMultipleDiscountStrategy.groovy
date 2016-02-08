package com.epam.trainings.spring.core.dm.service.impl.strategies

import com.epam.trainings.spring.core.dm.dao.TicketDao
import com.epam.trainings.spring.core.dm.model.Rating
import org.junit.Before
import org.junit.Test

import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.createEvent
import static com.epam.trainings.spring.core.dm.Utils.createTicket
import static com.epam.trainings.spring.core.dm.Utils.createUser
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class TestNthMultipleDiscountStrategy {

    NthMultipleTicketDiscountStrategy discountStrategy
    double discountPercentage = 0.5
    double ticketsToDiscount = 2
    TicketDao ticketDao

    @Before
    void init() {
        ticketDao = mock(TicketDao.class)

        discountStrategy = NthMultipleTicketDiscountStrategy.newInstance()
        discountStrategy.discountPercentage = discountPercentage
        discountStrategy.ticketDao = ticketDao
        discountStrategy.ticketsToDiscount = ticketsToDiscount
    }

    @Test
    void testGetDiscountForTheNthTicket() {
        def price = 100, discount = 50D, dateTime = LocalDateTime.now(),
            user = createUser(1, "name", "email", dateTime.toLocalDate()),
            event = createEvent("eName", price, Rating.HIGH),
            tickets = [createTicket(1, 1, null, null, null, 0)]
        when(ticketDao.findByUserId(user.id)).thenReturn(tickets)

        assert discount == discountStrategy.getDiscount(user, event, dateTime)
    }

    @Test
    void testGetDiscountForThe1stTicket() {
        def price = 100, discount = 0D, dateTime = LocalDateTime.now(),
            user = createUser(1, "name", "email", dateTime.toLocalDate()),
            event = createEvent("eName", price, Rating.HIGH),
            tickets = []
        when(ticketDao.findByUserId(user.id)).thenReturn(tickets)

        assert discount == discountStrategy.getDiscount(user, event, dateTime)
    }

    @Test
    void testGetDiscountForAnonymous() {
        def price = 100, discount = 0D, dateTime = LocalDateTime.now(),
            event = createEvent("eName", price, Rating.HIGH)

        assert discount == discountStrategy.getDiscount(null, event, dateTime)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetDiscountIncorrectEvent() {
        discountStrategy.getDiscount null, null, LocalDateTime.now()
    }

}
