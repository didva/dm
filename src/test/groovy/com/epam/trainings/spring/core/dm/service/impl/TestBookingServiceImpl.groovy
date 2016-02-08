package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao
import com.epam.trainings.spring.core.dm.dao.TicketDao
import com.epam.trainings.spring.core.dm.model.*
import com.epam.trainings.spring.core.dm.service.DiscountService
import org.junit.Before
import org.junit.Test

import java.time.LocalDate
import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.*
import static org.mockito.Mockito.*

class TestBookingServiceImpl {

    BookingServiceImpl bookingService

    DiscountService discountService
    TicketDao ticketDao
    AssignedEventsDao assignedEventsDao
    Event event
    Ticket ticket
    User user
    Seat seat

    @Before
    void init() {
        discountService = mock(DiscountService.class)
        assignedEventsDao = mock(AssignedEventsDao.class)
        ticketDao = mock(TicketDao.class)
        event = createEvent "e1", 100, Rating.HIGH
        ticket = createTicket 1, event.id, LocalDateTime.now(), null, [1, 2, 3], 0
        user = createUser 1, "u1", "email", LocalDate.now()
        seat = createSeat 1, false

        bookingService = BookingServiceImpl.newInstance()
        bookingService.ticketDao = ticketDao
        bookingService.discountService = discountService
        bookingService.assignedEventsDao = assignedEventsDao
    }

    @Test
    void testGetPrice() {
        def discountInPercentage = 0.5D, seats = [seat, createSeat(2, true)], dateTime = LocalDateTime.now(),
            totalPrice = discountInPercentage * event.rating.multiplier * (event.price + event.price * 2)
        when(discountService.getDiscount(user, event, dateTime)).thenReturn discountInPercentage * event.price
        assert totalPrice == bookingService.getTicketPrice(event, dateTime, seats, user)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectEvent() {
        bookingService.getTicketPrice null, LocalDateTime.now(), [seat], user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectDateTime() {
        bookingService.getTicketPrice event, null, [seat], user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectSeats() {
        bookingService.getTicketPrice event, LocalDateTime.now(), null, user
    }

    @Test
    void testBookTicket() {
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime))
                .thenReturn(createAssignedEvent(event, null, ticket.eventDateTime))
        when(ticketDao.findByEvent(event, ticket.eventDateTime)).thenReturn(Collections.emptyList())

        bookingService.bookTicket user, ticket
        verify(ticketDao, times(1)).add user, ticket
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketForUnexistingEvent() {
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn null
        bookingService.bookTicket user, ticket
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketForBookedSeats() {
        def bookedTicket = createTicket(1, event.id, ticket.eventDateTime, null, [1], 0)
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime))
                .thenReturn(createAssignedEvent(event, null, ticket.eventDateTime))
        when(ticketDao.findByEvent(event, ticket.eventDateTime)).thenReturn([bookedTicket])

        bookingService.bookTicket user, ticket
    }

    @Test(expected = IllegalArgumentException.class)
    void testBookTicketIncorrectTicket() {
        bookingService.bookTicket user, null
    }

    @Test
    void testGetTicketsForEvent() {
        def dateTime = LocalDateTime.now(), tickets = [ticket]
        when(ticketDao.findByEvent(event, dateTime)).thenReturn tickets

        assert tickets == bookingService.getTicketsForEvent(event, dateTime)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetTicketsForEventIncorrectEvent() {
        bookingService.getTicketsForEvent null, LocalDateTime.now()
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetTicketsForEventIncorrectDateTime() {
        bookingService.getTicketsForEvent event, null
    }

}
