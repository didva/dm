package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.model.AssignedEvent
import com.epam.trainings.spring.core.dm.model.Auditorium
import com.epam.trainings.spring.core.dm.model.Event
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.model.User
import org.junit.Before
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
class TestBookingService {

    @Autowired
    BookingService bookingService
    @Autowired
    UserService userService
    @Autowired
    EventService eventService
    @Autowired
    AuditoriumService auditoriumService

    static User user
    static Event event
    static AssignedEvent assignedEvent
    static Auditorium auditorium
    static LocalDateTime eventDateTime

    private static boolean setUpIsDone = false;

    @Before
    void init() {
        if (setUpIsDone) {
            return;
        }
        auditorium = auditoriumService.getAuditorium "testAuditoriumC"

        user = createUser 0, "name.TestBookingService", "email.TestBookingService", LocalDate.now().plusDays(2)
        userService.register user

        event = createEvent "event.TestBookingService", 100, Rating.HIGH
        eventService.create event

        eventDateTime = LocalDateTime.now()
        eventService.assignAuditorium event, auditorium, eventDateTime
        assignedEvent = eventService.getAssignedEvent event.id, eventDateTime

        setUpIsDone = true
    }

    @Test
    void testGetTicketPrice() {
        def seats = [10, 1]
        double expectedPrice = (event.price + event.price * 2) * event.rating.multiplier
        assert expectedPrice == bookingService.getTicketPrice(assignedEvent, seats as Set, user)
    }

    @Test
    void testGetTicketPriceWithDiscount() {
        def seats = [10, 1], discountPercentage = 0.95D, dateTime = user.birthDate.atStartOfDay(),
            expectedPrice = (event.price + event.price * 2) * event.rating.multiplier * discountPercentage

        eventService.assignAuditorium event, auditorium, dateTime
        def assignedEvent = eventService.getAssignedEvent event.id, dateTime
        assert expectedPrice == bookingService.getTicketPrice(assignedEvent, seats as Set, user)
    }

    @Test
    void testBookTicket() {
        def seats1 = [9, 10], seats2 = [1, 4],
            finalPrice1 = bookingService.getTicketPrice(assignedEvent, seats1 as Set, user),
            finalPrice2 = bookingService.getTicketPrice(assignedEvent, seats2 as Set, null),
            ticket1 = createTicket(1, assignedEvent.id, user.id, seats1, finalPrice1),
            ticket2 = createTicket(2, assignedEvent.id, null, seats2, finalPrice2)

        bookingService.bookTicket assignedEvent, seats1 as Set, user
        bookingService.bookTicket assignedEvent, seats2 as Set, null

        def actualTickets = bookingService.getTicketsForEvent event, eventDateTime
        assert [ticket1, ticket2].containsAll(actualTickets)
        assert 2 == actualTickets.size()
    }

}
