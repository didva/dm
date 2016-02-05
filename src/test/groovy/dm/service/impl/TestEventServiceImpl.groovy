package dm.service.impl

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao
import com.epam.trainings.spring.core.dm.dao.EventDao
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.AssignedEvent
import com.epam.trainings.spring.core.dm.model.Auditorium
import com.epam.trainings.spring.core.dm.model.Event
import com.epam.trainings.spring.core.dm.model.Rating
import com.epam.trainings.spring.core.dm.service.impl.EventServiceImpl
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatcher

import java.time.LocalDateTime

import static org.mockito.Mockito.*

class TestEventServiceImpl {

    EventServiceImpl eventService;
    Event event;
    EventDao eventDao
    AssignedEventsDao assignedEventsDao

    @Before
    void init() {
        eventDao = mock(EventDao.class)
        assignedEventsDao = mock(AssignedEventsDao.class)
        eventService = EventServiceImpl.newInstance()
        eventService.eventDao = eventDao
        eventService.assignedEventsDao = assignedEventsDao

        event = createEvent "test_name", 12.5, Rating.HIGH
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
    void testDeleteUnexistingEvent() {
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
        def events = [event, createEvent("newName", 12.3, Rating.LOW)]
        when(eventDao.findAll()).thenReturn events

        assert events == eventService.getAll()
        verify(eventDao, times(1)).findAll()
    }

    @Test
    void testGetForDateRange() {
        def from = LocalDateTime.now(), to = from.plusDays(2),
            assignedAudits = [createAssignedEvent(event, createAuditorium("aName"), from.plusDays(1))]
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
            assignedAudits = [createAssignedEvent(event, createAuditorium("aName"), fromMin.plusDays(1))]
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
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1")
        when(eventDao.find(event.id)).thenReturn event

        eventService.assignAuditorium event, auditorium, time
        verify(assignedEventsDao, times(1)).assignAuditorium(event, auditorium, time)
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalEvent() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1")
        eventService.assignAuditorium null, auditorium, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalAuditorium() {
        def time = LocalDateTime.now()
        eventService.assignAuditorium event, null, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalAuditoriumName() {
        def time = LocalDateTime.now(), auditorium = createAuditorium(null)
        eventService.assignAuditorium event, auditorium, time
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumIllegalDateTime() {
        def auditorium = createAuditorium("a_name1")
        eventService.assignAuditorium event, auditorium, null
    }

    @Test(expected = IllegalArgumentException.class)
    void testAssignAuditoriumUnexistingEvent() {
        def time = LocalDateTime.now(), auditorium = createAuditorium("a_name1")
        when(eventDao.find(event.id)).thenReturn null

        eventService.assignAuditorium event, auditorium, time
    }

    def createEvent = { String name, double price, Rating rating ->
        event = Event.newInstance();
        event.name = name;
        event.price = price;
        event.rating = rating
        event
    }

    def createAssignedEvent = { event, auditorium, LocalDateTime dateTime ->
        def assignedEvent = AssignedEvent.newInstance();
        assignedEvent.event = event;
        assignedEvent.auditorium = auditorium
        assignedEvent.dateTime = dateTime
        assignedEvent
    }

    def createAuditorium = { name ->
        def auditorium = Auditorium.newInstance();
        auditorium.name = name;
        auditorium
    }
}
