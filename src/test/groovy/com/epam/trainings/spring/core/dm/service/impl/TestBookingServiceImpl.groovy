package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.dao.TicketsDao
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.*
import com.epam.trainings.spring.core.dm.service.AuditoriumService
import com.epam.trainings.spring.core.dm.service.DiscountService
import com.epam.trainings.spring.core.dm.service.EventService
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
    EventService eventService

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
        eventService = mock EventService
        ticketDao = mock(TicketsDao.class)

        event = createEvent "e1", 100, Rating.HIGH
        auditorium = createAuditorium "auditorium1", 10, [3, 4, 5]
        user = createUser 1, "u1", "email", LocalDate.now()
        assignedEvent = createAssignedEvent event.id, auditorium.name, LocalDateTime.now()
        seat = 1
        ticket = createTicket 0, assignedEvent.id, user.id, [seat], 120

        bookingService = BookingServiceImpl.newInstance()
        bookingService.ticketsDao = ticketDao
        bookingService.discountService = discountService
        bookingService.auditoriumService = auditoriumService
        bookingService.eventService = eventService
    }

    @Test
    void testGetPrice() {
        def discountInPercentage = 0.5D, seats = [2, 3],
            totalPrice = discountInPercentage * event.rating.multiplier * (event.price + event.price * 2)
        when(eventService.getById(event.id)).thenReturn event
        when(discountService.getDiscount(user, event, assignedEvent.dateTime)).thenReturn discountInPercentage * event.price
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        assert totalPrice == bookingService.getTicketPrice(assignedEvent, seats as Set, user)
    }

    @Test(expected = IllegalArgumentException)
    void testGetPriceForIncorrectAssignedEvent() {
        bookingService.getTicketPrice(null, [seat] as Set, user)
    }

    @Test(expected = IllegalStateException)
    void testGetPriceForNonExistingEvent() {
        bookingService.getTicketPrice(assignedEvent, [seat] as Set, user)
    }

    @Test(expected = IllegalArgumentException)
    void testGetPriceForNonExistingSeat() {
        seat = auditorium.seatsNumber + 1
        when(eventService.getById(event.id)).thenReturn event
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.getTicketPrice assignedEvent, [seat] as Set, user
    }

    @Test(expected = AlreadyExistsException)
    void testGetPriceForAlreadyBooked() {
        def seats = [seat], bookedTicket = createTicket(1, assignedEvent.id, null, seats, 0)
        when(eventService.getById(event.id)).thenReturn event
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn([bookedTicket])

        bookingService.getTicketPrice assignedEvent, seats as Set, user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectEvent() {
        bookingService.getTicketPrice null, [seat] as Set, user
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetPriceIncorrectSeats() {
        bookingService.getTicketPrice assignedEvent, null, user
    }

    @Test
    void testBookTicket() {
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn Collections.emptyList()
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium
        when(eventService.getById(assignedEvent.eventId)).thenReturn event

        bookingService.bookTicket assignedEvent, [seat] as Set, user
        verify(ticketDao, times(1)).add user, ticket
    }

    @Test(expected = AlreadyExistsException)
    void testBookTicketForBookedSeats() {
        def bookedTicket = createTicket(1, assignedEvent.id, null, [seat], 0)
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn([bookedTicket])
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket assignedEvent, [seat] as Set, user
    }

    @Test(expected = IllegalArgumentException)
    void testBookTicketNonExistingSeat() {
        seat = auditorium.seatsNumber + 1
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn Collections.emptyList()
        when(auditoriumService.getAuditorium(auditorium.name)).thenReturn auditorium

        bookingService.bookTicket assignedEvent, [seat] as Set, user
    }

    @Test(expected = IllegalArgumentException.class)
    void testBookTicketIncorrectTicket() {
        bookingService.bookTicket null, [seat] as Set, user
    }

    @Test
    void testGetTicketsForEvent() {
        def dateTime = LocalDateTime.now(), tickets = [ticket]
        when(eventService.getAssignedEvent(event.id, dateTime)).thenReturn assignedEvent
        when(ticketDao.findByEvent(assignedEvent.id)).thenReturn tickets

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
