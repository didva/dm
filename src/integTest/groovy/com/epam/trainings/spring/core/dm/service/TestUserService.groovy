package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.model.User
import org.junit.Before
import org.junit.Test
import org.springframework.context.support.GenericGroovyApplicationContext

import java.time.LocalDate
import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.*

class TestUserService {

    User user
    UserService userService
    BookingService bookingService
    EventService eventService
    AuditoriumService auditoriumService

    @Before
    void init() {
        def context = new GenericGroovyApplicationContext("classpath:context.groovy");
        user = createUser(0, "test_name", "test_email", LocalDate.now());
        userService = context.getBean(UserService);
        bookingService = context.getBean(BookingService)
        eventService = context.getBean(EventService)
        auditoriumService = context.getBean(AuditoriumService)
    }

    @Test
    void testRegisterUser() {
        assert null == userService.getUserByEmail(user.getEmail())
        userService.register(user)
        assert user.id != 0
        assert user == userService.getUserByEmail(user.getEmail())
    }

    @Test(expected = AlreadyExistsException)
    void testRegisterUserWithSameEmail() {
        userService.register(user)
        userService.register(user)
    }

    @Test
    void testDeleteUser() {
        userService.register(user)
        userService.remove(user.id)
        assert null == userService.getUserByEmail(user.getEmail())
    }

    @Test(expected = IllegalArgumentException)
    void testDeleteUnexistingUser() {
        userService.remove(1)
    }

    @Test
    void testGetById() {
        userService.register(user)
        assert user == userService.getById(user.id)
    }

    @Test
    void testGetUsersByName() {
        def user2 = createUser(0, user.name, "some_email1", LocalDate.now()),
            user3 = createUser(0, user.name + "new", "some_email2", LocalDate.now())
        userService.register(user)
        userService.register(user2)
        userService.register(user3)

        assert [user, user2] == userService.getUsersByName(user.name)
    }

    @Test
    void testGetBookedTickets() {
        def eventDateTime = LocalDateTime.now(), event = createEvent("testEvent", 100D, Rating.HIGH),
            auditorium = auditoriumService.getAuditorium("auditoriumC"),
            seat1 = createSeat(1, false), seat2 = createSeat(2, true),
            finalPrice = bookingService.getTicketPrice(event, eventDateTime, [seat1, seat2], user)

        eventService.create event
        eventService.assignAuditorium event, auditorium, eventDateTime
        userService.register user

        def ticket = createTicket(1, event.id, eventDateTime, user.id, [seat1, seat2], finalPrice)
        bookingService.bookTicket user, ticket
        assert [ticket] == userService.getBookedTickets(user.id)
    }
}
