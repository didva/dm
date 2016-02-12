package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.model.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.time.LocalDate
import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.*

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("classpath:context.groovy")
class TestUserService {

    @Autowired
    UserService userService
    @Autowired
    BookingService bookingService
    @Autowired
    EventService eventService
    @Autowired
    AuditoriumService auditoriumService

    @Test
    void testRegisterUser() {
        User user = createUser(0, "testRegisterUser.name", "testRegisterUser.email", LocalDate.now())
        assert null == userService.getUserByEmail(user.getEmail())
        userService.register(user)
        assert user.id != 0
        assert user == userService.getUserByEmail(user.getEmail())
    }

    @Test(expected = AlreadyExistsException)
    void testRegisterUserWithSameEmail() {
        User user = createUser(0, "testRegisterUserWithSameEmail.name", "testRegisterUserWithSameEmail.email", LocalDate.now())
        userService.register(user)
        userService.register(user)
    }

    @Test
    void testDeleteUser() {
        User user = createUser(0, "testDeleteUser.name", "testDeleteUser.email", LocalDate.now())
        userService.register(user)
        userService.remove(user.id)
        assert null == userService.getUserByEmail(user.getEmail())
    }

    @Test(expected = IllegalArgumentException)
    void testDeleteNonExistingUser() {
        User user = createUser(0, "testDeleteNonExistingUser.name", "testDeleteNonExistingUser.email", LocalDate.now())
        userService.register(user)
        userService.remove(user.id)
        userService.remove(user.id)
    }

    @Test
    void testGetById() {
        User user = createUser(0, "testGetById.name", "testGetById.email", LocalDate.now())
        userService.register(user)
        assert user == userService.getById(user.id)
    }

    @Test
    void testGetUsersByName() {
        def user1 = createUser(0, "testGetUsersByName.name1", "testGetUsersByName.email1", LocalDate.now()),
            user2 = createUser(0, "testGetUsersByName.name1", "testGetUsersByName.email2", LocalDate.now()),
            user3 = createUser(0, "testGetUsersByName.name3", "testGetUsersByName.email3", LocalDate.now())

        userService.register(user1)
        userService.register(user2)
        userService.register(user3)

        assert [user1, user2] == userService.getUsersByName(user1.name)
    }

    @Test
    void testGetBookedTickets() {
        User user = createUser(0, "testGetBookedTickets.name", "testGetBookedTickets.email", LocalDate.now())
        def eventDateTime = LocalDateTime.now(), event = createEvent("event.testGetBookedTickets", 100D, Rating.HIGH),
            auditorium = auditoriumService.getAuditorium("testAuditoriumC"), seats = [10, 2]

        eventService.create event
        eventService.assignAuditorium event, auditorium, eventDateTime
        userService.register user

        def assignedEvent = eventService.getAssignedEvent(event.id, eventDateTime),
            finalPrice = bookingService.getTicketPrice(assignedEvent, seats as Set, user),
            ticket = createTicket(1, assignedEvent.id, user.id, seats, finalPrice)

        bookingService.bookTicket assignedEvent, seats as Set, user

        assert [ticket] == userService.getBookedTickets(user.id)
    }
}
