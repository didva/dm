package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.Utils
import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao
import com.epam.trainings.spring.core.dm.dao.EventDao
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.AssignedEvent
import com.epam.trainings.spring.core.dm.model.Event
import com.epam.trainings.spring.core.dm.model.Rating
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatcher

import java.time.LocalDateTime

import static com.epam.trainings.spring.core.dm.Utils.createAssignedEvent
import static com.epam.trainings.spring.core.dm.Utils.createAuditorium
import static org.mockito.Mockito.*

class TestEventServiceImpl {

    EventServiceImpl eventService;

    EventDao eventDao
    AssignedEventsDao assignedEventsDao

    Event event;
    AssignedEvent assignedEvent

    @Before
    void init() {
        eventDao = mock(EventDao.class)
        assignedEventsDao = mock(AssignedEventsDao.class)
        eventService = EventServiceImpl.newInstance()
        eventService.eventDao = eventDao
        eventService.assignedEventsDao = assignedEventsDao

        event = Utils.createEvent "test_name", 12.5, Rating.HIGH
        assignedEvent = createAssignedEvent event.id, "name", LocalDateTime.now()
    }

    @Test
    void testCreate() {
        eventService.create event
        verify(eventDao, times(1)).add event
    }

    @Test(expected = AlreadyExistsException.class)
    void testCreateDuplicated() {
        when(eventDao.findByName(event.name)).thenReturn event
        eventService.create event

    }

    @Test(expected = IllegalArgumentException.class)
    void testCreateWithIncorrectArgument() {
        eventService.create null
    }

    @Test(expected = IllegalArgumentException.class)
    void testCreateWithIncorrectEventName() {
        event.name = null
        eventService.create event
    }

    @Test(expected = IllegalArgumentException.class)
    void testCreateWithIncorrectRating() {
        event.rating = null
        eventService.create event
    }

    @Test
    void testDeleteEvent() {
        when(eventDao.find(event.id)).thenReturn event

        eventService.remove event.id
        verify(eventDao, times(1)).delete event.id
    }

    @Test(expected = IllegalArgumentException.class)
    void testDeleteNonExistingEvent() {
        final long id = 1
        when(eventDao.find(id)).thenReturn null

        eventService.remove id
    }

    @Test
    void testGetByName() {
        when(eventDao.findByName(event.name)).thenReturn event
        assert event == eventService.getByName(event.name)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetByNameWithIncorrectArg() {
        eventService.getByName null
    }

    @Test
    void testGetAll() {
        def events = [event, Utils.createEvent("newName", 12.3, Rating.LOW)]
        when(eventDao.findAll()).thenReturn events

        assert events == eventService.getAll()
        verify(eventDao, times(1)).findAll()
    }

    @Test
    void testGetAssignedEvent() {
        when(assignedEventsDao.findByEvent(event.id, assignedEvent.dateTime)).thenReturn assignedEvent
        when(eventDao.find(event.id)).thenReturn event

        assert assignedEvent == eventService.getAssignedEvent(event.id, assignedEvent.dateTime)
    }

    @Test(expected = IllegalArgumentException)
    void testGetAssignedEventIncorrectDateTime() {
        eventService.getAssignedEvent event.id, null
    }

    @Test(expected = IllegalArgumentException)
    void testGetAssignedEventNonExistingEvent() {
        eventService.getAssignedEvent 10, LocalDateTime.now()
    }

    @Test
    void testGetAllAssignedEvents() {
        when(assignedEventsDao.findAll()).thenReturn([assignedEvent])
        assert [assignedEvent] == eventService.getAllAssignedEvents()
    }

    @Test
    void testGetForDateRange() {
        def from = LocalDateTime.now(), to = from.plusDays(2),
            assignedAudits = [createAssignedEvent(event.id, "aName", from.plusDays(1))]
        when(assignedEventsDao.findByRange(from, to)).thenReturn assignedAudits

        assert assignedAudits == eventService.getForDateRange(from, to)
        verify(assignedEventsDao, times(1)).findByRange(from, to)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetForDateRangeIllegalFrom() {
        eventService.getForDateRange null, LocalDateTime.now()
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetForDateRangeIllegalTo() {
        eventService.getForDateRange LocalDateTime.now(), null
    }

    @Test
    void testGetNextEvents() {
        def deviationSeconds = 5
        def fromMin = LocalDateTime.now(), fromMax = fromMin.plusSeconds(deviationSeconds), to = fromMin.plusDays(2),
            assignedAudits = [createAssignedEvent(event.id, "aName", fromMin.plusDays(1))]
        when(assignedEventsDao.findByRange(argThat(new ArgumentMatcher() {

            @Override
            boolean matches(Object argument) {
                LocalDateTime dateTime = (LocalDateTime) argument
                dateTime.isBefore(fromMax) && dateTime.isAfter(fromMin)
            }
        }) as LocalDateTime, eq(to))).thenReturn assignedAudits

        assert assignedAudits == eventService.getNextEvents(to)
        verify(assignedEventsDao, times(1)).findByRange(any(LocalDateTime.class), any(LocalDateTime.class))
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetNextEventsIllegalTo() {
        eventService.getNextEvents null
    }

    @Test
    void testAssignAuditorium() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1", 1, [])
        when(eventDao.find(event.id)).thenReturn event

        eventService.assignAuditorium event, auditorium, time
        verify(assignedEventsDao, times(1)).assignAuditorium(new AssignedEvent(event.id, auditorium.name, time))
    }

    @Test(expected = AlreadyExistsException)
    void testAssignAuditoriumWhenAuditoriumAlreadyAssigned() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1", 1, [])
        when(eventDao.find(event.id)).thenReturn event
        when(assignedEventsDao.findByAuditorium(auditorium.name, time)).thenReturn assignedEvent

        eventService.assignAuditorium event, auditorium, time
    }

    @Test(expected = AlreadyExistsException)
    void testAssignAuditoriumWhenEventForThisTimeAlreadyAssigned() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1", 1, [])
        when(eventDao.find(event.id)).thenReturn event
        when(assignedEventsDao.findByEvent(event.id, time)).thenReturn assignedEvent

        eventService.assignAuditorium event, auditorium, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalEvent() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1", 1, [])
        eventService.assignAuditorium null, auditorium, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalAuditorium() {
        def time = LocalDateTime.now()
        eventService.assignAuditorium event, null, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalAuditoriumName() {
        def time = LocalDateTime.now(), auditorium = createAuditorium(null, 1, [])
        eventService.assignAuditorium event, auditorium, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalDateTime() {
        def auditorium = createAuditorium("a_name1", 1, [])
        eventService.assignAuditorium event, auditorium, null
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumNonExistingEvent() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1", 1, [])
        when(eventDao.find(event.id)).thenReturn null

        eventService.assignAuditorium event, auditorium, time
    }

}
