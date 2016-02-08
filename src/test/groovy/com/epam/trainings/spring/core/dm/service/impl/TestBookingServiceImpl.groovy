package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao
import com.epam.trainings.spring.core.dm.dao.TicketDao
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.*
import com.epam.trainings.spring.core.dm.service.AuditoriumService
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
    AuditoriumService auditoriumService
    TicketDao ticketDao
    AssignedEventsDao assignedEventsDao

    Event event
    Ticket ticket
    User user
    Seat seat
    Auditorium auditorium
    AssignedEvent assignedEvent

    @Before
    void init() {
        discountService = mock DiscountService
        auditoriumService = mock AuditoriumService
        assignedEventsDao = mock AssignedEventsDao
        ticketDao = mock(TicketDao.class)
        event = createEvent "e1", 100, Rating.HIGH
        auditorium = createAuditorium("auditorium1", 10, [3, 4, 5] as Set)
        user = createUser 1, "u1", "email", LocalDate.now()
        seat = createSeat 1, 1, false, auditorium.name
        ticket = createTicket 1, event.id, LocalDateTime.now(), null, [seat], 0
        assignedEvent = createAssignedEvent event, auditorium, ticket.eventDateTime

        bookingService = BookingServiceImpl.newInstance()
        bookingService.ticketDao = ticketDao
        bookingService.discountService = discountService
        bookingService.auditoriumService = auditoriumService
        bookingService.assignedEventsDao = assignedEventsDao
    }

    @Test
    void testGetPrice() {
        def discountInPercentage = 0.5D, seats = [createSeat(2, 2, false, auditorium.name), createSeat(3, 3, true, auditorium.name)],
            dateTime = LocalDateTime.now(), totalPrice = discountInPercentage * event.rating.multiplier * (event.price + event.price * 2)
        when(discountService.getDiscount(user, event, dateTime)).thenReturn discountInPercentage * event.price
        when(assignedEventsDao.findByEvent(event.id, dateTime)).thenReturn assignedEvent
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        assert totalPrice == bookingService.getTicketPrice(event, dateTime, seats as Set, user)
    }

    @Test(expected = IllegalArgumentException)
    void testGetPriceForNonExistingEvent() {
        def seats = [seat], dateTime = LocalDateTime.now()
        bookingService.getTicketPrice(event, dateTime, seats as Set, user)
    }

    @Test(expected = IllegalArgumentException)
    void testGetPriceForNonExistingSeat() {
        seat.number = 100
        def seats = [seat], dateTime = LocalDateTime.now()
        when(assignedEventsDao.findByEvent(event.id, dateTime)).thenReturn assignedEvent
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.getTicketPrice event, dateTime, seats as Set, user
    }

    @Test(expected = AlreadyExistsException)
    void testGetPriceForAlreadyBooked() {
        def seats = [seat], dateTime = LocalDateTime.now(), bookedTicket = createTicket(1, event.id, dateTime, null, [seat], 0)
        when(assignedEventsDao.findByEvent(event.id, dateTime)).thenReturn assignedEvent
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium
        when(ticketDao.findByEvent(event.id, dateTime)).thenReturn([bookedTicket])

        bookingService.getTicketPrice event, dateTime, seats as Set, user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectEvent() {
        bookingService.getTicketPrice null, LocalDateTime.now(), [seat] as Set, user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectDateTime() {
        bookingService.getTicketPrice event, null, [seat] as Set, user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectSeats() {
        bookingService.getTicketPrice event, LocalDateTime.now(), null, user
    }

    @Test
    void testBookTicket() {
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn assignedEvent
        when(ticketDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn Collections.emptyList()
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket user, ticket
        verify(ticketDao, times(1)).add user, ticket
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketForNonExistingEvent() {
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn null
        bookingService.bookTicket user, ticket
    }

    @Test(expected = AlreadyExistsException)
    void testBookTicketForBookedSeats() {
        def bookedTicket = createTicket(1, event.id, ticket.eventDateTime, null, [seat], 0)
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn assignedEvent
        when(ticketDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn([bookedTicket])
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket user, ticket
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketNonExistingSeat() {
        seat.number = auditorium.seatsNumber + 1
        def ticket = createTicket(1, event.id, ticket.eventDateTime, null, [seat], 0)
        when(assignedEventsDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn assignedEvent
        when(ticketDao.findByEvent(event.id, ticket.eventDateTime)).thenReturn Collections.emptyList()
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket user, ticket
    }

    @Test(expected = IllegalArgumentException.class)
    void testBookTicketIncorrectTicket() {
        bookingService.bookTicket user, null
    }

    @Test
    void testGetTicketsForEvent() {
        def dateTime = LocalDateTime.now(), tickets = [ticket]
        when(ticketDao.findByEvent(event.id, dateTime)).thenReturn tickets
        when(assignedEventsDao.findByEvent(event.id, dateTime)).thenReturn assignedEvent

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
