package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.model.*
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy
import org.junit.Before
import org.junit.Test
import org.springframework.context.support.GenericGroovyApplicationContext

import java.time.LocalDate
import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.*

class TestBookingService {

    BookingService bookingService
    UserService userService
    EventService eventService
    AuditoriumService auditoriumService

    User user
    Event event
    AssignedEvent assignedEvent
    Auditorium auditorium
    LocalDateTime eventDateTime
    BirthdayDiscountStrategy birthdayDiscountStrategy

    @Before
    void init() {
        def context = new GenericGroovyApplicationContext("classpath:context.groovy");

        bookingService = context.getBean BookingService
        userService = context.getBean UserService
        eventService = context.getBean EventService
        auditoriumService = context.getBean AuditoriumService

        user = createUser 0, "name", "email", LocalDate.now().plusDays(2)
        userService.register user

        auditorium = auditoriumService.getAuditorium "testAuditoriumC"

        event = createEvent "testEvent", 100, Rating.HIGH
        eventService.create event
        eventDateTime = LocalDateTime.now()
        eventService.assignAuditorium event, auditorium, eventDateTime
        assignedEvent = eventService.getAssignedEvent event.id, eventDateTime

        birthdayDiscountStrategy = context.getBean BirthdayDiscountStrategy
    }

    @Test
    void testGetTicketPrice() {
        def seats = [10, 1]
        double expectedPrice = (event.price + event.price * 2) * event.rating.multiplier
        assert expectedPrice == bookingService.getTicketPrice(event, eventDateTime, seats as Set, user)
    }

    @Test
    void testGetTicketPriceWithDiscount() {
        def seats = [10, 1], discountPercentage = 0.95D,
            expectedPrice = (event.price + event.price * 2) * event.rating.multiplier * discountPercentage

        eventService.assignAuditorium event, auditorium, user.birthDate.atStartOfDay()
        assert expectedPrice == bookingService.getTicketPrice(event, user.birthDate.atStartOfDay(), seats as Set, user)
    }

    @Test
    void testBookTicket() {
        def seats1 = [9, 10], seats2 = [1, 4],
            finalPrice1 = bookingService.getTicketPrice(event, eventDateTime, seats1 as Set, user),
            finalPrice2 = bookingService.getTicketPrice(event, eventDateTime, seats2 as Set, null),
            ticket1 = createTicket(1, assignedEvent.id, user.id, seats1, finalPrice1),
            ticket2 = createTicket(2, assignedEvent.id, user.id, seats2, finalPrice2)

        bookingService.bookTicket user, ticket1
        bookingService.bookTicket null, ticket2

        def actualTickets = bookingService.getTicketsForEvent event, eventDateTime
        assert [ticket1, ticket2].containsAll(actualTickets)
        assert 2 == actualTickets.size()
    }

}
