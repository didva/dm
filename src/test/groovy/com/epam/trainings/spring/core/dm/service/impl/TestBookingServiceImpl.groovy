package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao
import com.epam.trainings.spring.core.dm.dao.TicketsDao
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
    TicketsDao ticketDao
    AssignedEventsDao assignedEventsDao

    Event event
    Ticket ticket
    User user
    Auditorium auditorium
    AssignedEvent assignedEvent
    Integer seat

    @Before
    void init() {
        discountService = mock DiscountService
        auditoriumService = mock AuditoriumService
        assignedEventsDao = mock AssignedEventsDao
        ticketDao = mock(TicketsDao.class)

        event = createEvent "e1", 100, Rating.HIGH
        auditorium = createAuditorium "auditorium1", 10, [3, 4, 5]
        user = createUser 1, "u1", "email", LocalDate.now()
        assignedEvent = createAssignedEvent event.id, auditorium.name, LocalDateTime.now()
        seat = 1
        ticket = createTicket 1, assignedEvent.id, null, [seat], 0

        bookingService = BookingServiceImpl.newInstance()
        bookingService.ticketsDao = ticketDao
        bookingService.discountService = discountService
        bookingService.auditoriumService = auditoriumService
        bookingService.assignedEventsDao = assignedEventsDao
    }

    @Test
    void testGetPrice() {
        def discountInPercentage = 0.5D, seats = [2, 3],
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
        seat = auditorium.seatsNumber + 1
        def seats = [seat], dateTime = LocalDateTime.now()
        when(assignedEventsDao.findByEvent(event.id, dateTime)).thenReturn assignedEvent
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.getTicketPrice event, dateTime, seats as Set, user
    }

    @Test(expected = AlreadyExistsException)
    void testGetPriceForAlreadyBooked() {
        def seats = [seat], dateTime = LocalDateTime.now(), bookedTicket = createTicket(1, assignedEvent.id, null, [seat], 0)
        when(assignedEventsDao.findByEvent(event.id, dateTime)).thenReturn assignedEvent
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn([bookedTicket])

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
        when(assignedEventsDao.find(assignedEvent.id)).thenReturn assignedEvent
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn Collections.emptyList()
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket user, ticket
        verify(ticketDao, times(1)).add user, ticket
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketForNonExistingEvent() {
        when(assignedEventsDao.findByEvent(event.id, assignedEvent.dateTime)).thenReturn null
        bookingService.bookTicket user, ticket
    }

    @Test(expected = AlreadyExistsException)
    void testBookTicketForBookedSeats() {
        def bookedTicket = createTicket(1, assignedEvent.id, null, [seat], 0)
        when(assignedEventsDao.find(assignedEvent.id)).thenReturn assignedEvent
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn([bookedTicket])
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket user, ticket
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketNonExistingSeat() {
        seat = auditorium.seatsNumber + 1
        def ticket = createTicket(1, assignedEvent.id, null, [seat], 0)
        when(assignedEventsDao.findByEvent(event.id, assignedEvent.dateTime)).thenReturn assignedEvent
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn Collections.emptyList()
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
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn tickets
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
