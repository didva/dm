package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignedEventsDao {

    AssignedEvent find(long id);

    List<AssignedEvent> findAll();

    List<AssignedEvent> findByRange(LocalDateTime from, LocalDateTime to);

    void assignAuditorium(AssignedEvent assignedEvent);

    AssignedEvent findByEvent(long eventId, LocalDateTime dateTime);

    AssignedEvent findByAuditorium(String auditorium, LocalDateTime dateTime);

}
