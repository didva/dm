package com.epam.trainings.spring.core.dm.aspect

import com.epam.trainings.spring.core.dm.Utils
import com.epam.trainings.spring.core.dm.config.TestLuckyConfig
import com.epam.trainings.spring.core.dm.model.LuckyInfo
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.service.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.time.LocalDateTime

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestLuckyConfig])
class TestLuckyAspect {

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
    void testUserIsLucky() {
        def dateTime = LocalDateTime.now(), event = Utils.createEvent("name.testUserIsLucky", 100, Rating.HIGH),
            user = Utils.createUser(0, "name.testUserIsLucky", "email.testUserIsLucky", dateTime.toLocalDate())

        userService.register user
        eventService.create event
        eventService.assignAuditorium event, auditoriumService.getAuditorium("auditoriumA"), dateTime
        def assignedEvent = eventService.getAssignedEvent event.id, dateTime

        bookingService.bookTicket assignedEvent, [1, 2] as Set, user
        def ticket = bookingService.getTicketsForEvent(event, dateTime)[0],
            luckyInfo = new LuckyInfo(0, user.id, ticket.id)

        assert [luckyInfo] == statisticService.getLuckyInfoByEventId(event.id)
        assert [luckyInfo] == statisticService.getLuckyInfoByUserId(user.id)
        assert 0D == ticket.finalPrice
    }

}
