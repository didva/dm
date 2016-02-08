package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.Event
import com.epam.trainings.spring.core.dm.model.Rating
import org.junit.Before
import org.junit.Test
import org.springframework.context.support.GenericGroovyApplicationContext

import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.*

class TestEventService {

    Event event
    EventService eventService

    @Before
    void init() {
        def context = new GenericGroovyApplicationContext("classpath:context.groovy");

        event = createEvent "test_name", 100, Rating.HIGH
        eventService = context.getBean EventService.class
    }

    @Test
    void testCreate() {
        assert null == eventService.getByName(event.name)
        eventService.create event
        assert event.id != 0
        assert event == eventService.getByName(event.name)
    }

    @Test(expected = AlreadyExistsException)
    void testCreateDuplicated() {
        eventService.create event
        eventService.create event
    }

    @Test
    void testRemove() {
        eventService.create event
        eventService.remove event.id
        assert null == eventService.getByName(event.name)
    }

    @Test(expected = IllegalArgumentException)
    void testRemoveUnexisting() {
        eventService.remove 1
    }

    @Test
    void testGetAll() {
        def event2 = createEvent "event2", 200, Rating.LOW
        eventService.create event
        eventService.create event2

        def actualEvents = eventService.getAll();
        assert [event, event2].containsAll(actualEvents)
        assert 2 == actualEvents.size()
    }

    @Test
    void testAssignAuditorium() {
        def event2 = createEvent "event2", 200, Rating.LOW
        def dateTimeEvent1 = LocalDateTime.now(), dateTimeEvent2 = LocalDateTime.now().plusDays(2),
            assignedEvent1 = createAssignedEvent(event, createAuditorium("auditorium1", 1, [] as Set), dateTimeEvent1),
            assignedEvent2 = createAssignedEvent(event2, createAuditorium("auditorium2", 1, [] as Set), dateTimeEvent2)


        eventService.create event
        eventService.create event2

        eventService.assignAuditorium assignedEvent1.event, assignedEvent1.auditorium, assignedEvent1.dateTime
        eventService.assignAuditorium assignedEvent2.event, assignedEvent2.auditorium, assignedEvent2.dateTime

        assert [assignedEvent2] == eventService.getForDateRange(LocalDateTime.now(), dateTimeEvent2.plusSeconds(1))
    }
}
