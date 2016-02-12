package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.Rating
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.createAuditorium
import static com.epam.trainings.spring.core.dm.Utils.createEvent

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("classpath:context.groovy")
class TestEventService {

    @Autowired
    EventService eventService

    @Test
    void testCreate() {
        def event = createEvent "event.testCreate", 100, Rating.HIGH
        assert null == eventService.getByName(event.name)
        eventService.create event
        assert event.id != 0
        assert event == eventService.getByName(event.name)
    }

    @Test(expected = AlreadyExistsException)
    void testCreateDuplicated() {
        def event = createEvent "event.testCreateDuplicated", 100, Rating.HIGH
        eventService.create event
        eventService.create event
    }

    @Test
    void testRemove() {
        def event = createEvent "event.testRemove", 100, Rating.HIGH
        eventService.create event
        eventService.remove event.id
        assert null == eventService.getByName(event.name)
    }

    @Test(expected = IllegalArgumentException)
    void testRemoveNonExisting() {
        def event = createEvent "event.testRemoveNonExisting", 100, Rating.HIGH
        eventService.create event
        eventService.remove event.id
        eventService.remove event.id
    }

    @Test
    void testGetAll() {
        def event1 = createEvent("event.testGetAll1", 100, Rating.HIGH),
            event2 = createEvent "event.testGetAll2", 200, Rating.LOW
        eventService.create event1
        eventService.create event2

        def actualEvents = eventService.getAll();
        assert actualEvents.containsAll([event1, event2])
    }

    @Test
    void testAssignAuditorium() {
        def event1 = createEvent("event.testAssignAuditorium1", 100, Rating.HIGH),
            event2 = createEvent("event.testAssignAuditorium2", 200, Rating.LOW),
            dateTimeEvent1 = LocalDateTime.now(), dateTimeEvent2 = LocalDateTime.now().plusDays(2),
            auditorium1 = createAuditorium("auditorium1", 1, []), auditorium2 = createAuditorium("auditorium2", 1, [])

        eventService.create event1
        eventService.create event2

        eventService.assignAuditorium event1, auditorium1, dateTimeEvent1
        eventService.assignAuditorium event2, auditorium2, dateTimeEvent2

        def expected = [eventService.getAssignedEvent(event2.id, dateTimeEvent2)]
        assert eventService.getForDateRange(LocalDateTime.now(), dateTimeEvent2.plusSeconds(1)).containsAll(expected)
    }
}

