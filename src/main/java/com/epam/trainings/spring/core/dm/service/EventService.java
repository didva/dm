package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.annotations.TrackMethodExecutions;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import com.epam.trainings.spring.core.dm.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    void create(Event event);

    void remove(long id);

    @TrackMethodExecutions(description = "Search event by name")
    Event getByName(String name);

    List<Event> getAll();

    AssignedEvent getAssignedEvent(long eventId, LocalDateTime dateTime);

    List<AssignedEvent> getAllAssignedEvents();

    List<AssignedEvent> getForDateRange(LocalDateTime from, LocalDateTime to);

    List<AssignedEvent> getNextEvents(LocalDateTime to);

    void assignAuditorium(Event event, Auditorium auditorium, LocalDateTime dateTime);

}
