package com.epam.trainings.spring.core.dm.aspect

import com.epam.trainings.spring.core.dm.Utils
import com.epam.trainings.spring.core.dm.model.Counter
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.service.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.time.LocalDateTime

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("classpath:context.groovy")
class TestDiscountStatisticsAspect {

    @Autowired
    EventService eventService
    @Autowired
    BookingService bookingService
    @Autowired
    StatisticService statisticService
    @Autowired
    AuditoriumService auditoriumService
    @Autowired
    UserService userService

    @Test
    void testDiscountStatistics() {
        def dateTime = LocalDateTime.now(), event = Utils.createEvent("name.testDiscountStatistics", 100, Rating.HIGH),
            user = Utils.createUser(0, "name.testDiscountStatistics", "email.testDiscountStatistics", dateTime.toLocalDate())

        userService.register user
        eventService.create event
        eventService.assignAuditorium event, auditoriumService.getAuditorium("auditoriumA"), dateTime
        def assignedEvent = eventService.getAssignedEvent event.id, dateTime
        def counters = [new Counter<String>("Birthday Discount", 1)]

        bookingService.bookTicket assignedEvent, [1, 2] as Set, user
        assert counters == statisticService.getDiscountsByUser(user.id)
        assert counters == statisticService.getAllDiscounts()
    }


}
