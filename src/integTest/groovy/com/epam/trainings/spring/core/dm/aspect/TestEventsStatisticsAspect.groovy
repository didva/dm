package com.epam.trainings.spring.core.dm.aspect

import com.epam.trainings.spring.core.dm.Utils
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.service.AuditoriumService
import com.epam.trainings.spring.core.dm.service.BookingService
import com.epam.trainings.spring.core.dm.service.EventService
import com.epam.trainings.spring.core.dm.service.StatisticService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.time.LocalDateTime

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("classpath:context.groovy")
class TestEventsStatisticsAspect {

    @Autowired
    EventService eventService
    @Autowired
    BookingService bookingService
    @Autowired
    StatisticService eventsStatisticService
    @Autowired
    AuditoriumService auditoriumService

    @Test
    void testRequestsByName() {
        def event = Utils.createEvent "name.testGetEventByName", 100, Rating.HIGH
        eventService.create event
        eventService.getByName event.name
        assert 1 == eventsStatisticService.getRequestsByName(event.id).times
    }

    @Test
    void testRequestsForPrices() {
        def dateTime = LocalDateTime.now(), event = Utils.createEvent "name.testRequestsForPrices", 100, Rating.HIGH
        eventService.create event
        eventService.assignAuditorium event, auditoriumService.getAuditorium("auditoriumA"), dateTime
        def assignedEvent = eventService.getAssignedEvent event.id, dateTime

        bookingService.getTicketPrice assignedEvent, [1, 2] as Set, null
        assert 1 == eventsStatisticService.getRequestsForPrices(event.id).times
    }

    @Test
    void testBookedTimes() {
        def dateTime = LocalDateTime.now(), event = Utils.createEvent "name.testBookedTimes", 100, Rating.HIGH
        eventService.create event
        eventService.assignAuditorium event, auditoriumService.getAuditorium("auditoriumA"), dateTime
        def assignedEvent = eventService.getAssignedEvent event.id, dateTime

        bookingService.bookTicket assignedEvent, [1, 2] as Set, null
        assert 1 == eventsStatisticService.getBookedTimes(event.id).times
    }


}
