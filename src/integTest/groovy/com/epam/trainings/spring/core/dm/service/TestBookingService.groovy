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

        auditorium = auditoriumService.getAuditorium "auditoriumC"

        event = createEvent "testEvent", 100D, Rating.HIGH
        eventService.create event
        eventDateTime = LocalDateTime.now()
        eventService.assignAuditorium event, auditorium, eventDateTime

        birthdayDiscountStrategy = context.getBean BirthdayDiscountStrategy
    }

    @Test
    void testGetTicketPrice() {
        Seat seat1 = createSeat(1L, false), seat2 = createSeat(2, true)
        double expectedPrice = (event.price + event.price * 2) * event.rating.multiplier
        assert expectedPrice == bookingService.getTicketPrice(event, eventDateTime, [seat1, seat2], user)
    }

    @Test
    void testGetTicketPriceWithDiscount() {
        Seat seat1 = createSeat(1L, false), seat2 = createSeat(2, true)
        double expectedPrice = (event.price + event.price * 2) * event.rating.multiplier * (1 - birthdayDiscountStrategy.discountPercentage)
        assert expectedPrice == bookingService.getTicketPrice(event, user.birthDate.atStartOfDay(), [seat1, seat2], user)
    }

    @Test
    void testBookTicket() {
        def seat1 = createSeat(1, false), seat2 = createSeat(2, true),
            seat3 = createSeat(3, true), seat4 = createSeat(4, true)
        def finalPrice1 = bookingService.getTicketPrice(event, eventDateTime, [seat1, seat2], user),
            finalPrice2 = bookingService.getTicketPrice(event, eventDateTime, [seat3, seat4], null)
        def ticket1 = createTicket(1, event.id, eventDateTime, user.id, [seat1, seat2], finalPrice1),
            ticket2 = createTicket(2, event.id, eventDateTime, user.id, [seat3, seat4], finalPrice2)

        bookingService.bookTicket user, ticket1
        bookingService.bookTicket null, ticket2

        def actualTickets = bookingService.getTicketsForEvent event, eventDateTime
        assert [ticket1, ticket2].containsAll(actualTickets)
        assert 2 == actualTickets.size()
    }

}
