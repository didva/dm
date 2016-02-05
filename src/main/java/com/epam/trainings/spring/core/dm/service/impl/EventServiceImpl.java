package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

public class EventServiceImpl implements EventService {

    private EventDao eventDao;
    private AssignedEventsDao assignedEventsDao;

    @Override
    public void create(Event event) {
        if (event == null || event.getName() == null || event.getRating() == null) {
            throw new IllegalArgumentException();
        }
        if (eventDao.findByName(event.getName()) != null) {
            throw new AlreadyExistsException();
        }
        eventDao.add(event);
    }

    @Override
    public void remove(long id) {
        Event event = eventDao.find(id);
        if (event == null) {
            throw new IllegalArgumentException();
        }
        eventDao.delete(id);
    }

    @Override
    public Event getByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        return eventDao.findByName(name);
    }

    @Override
    public List<Event> getAll() {
        return eventDao.findAll();
    }

    @Override
    public List<AssignedEvent> getForDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException();
        }
        return assignedEventsDao.findByRange(from, to);
    }

    @Override
    public List<AssignedEvent> getNextEvents(LocalDateTime to) {
        return this.getForDateRange(LocalDateTime.now(), to);
    }

    @Override
    public void assignAuditorium(Event event, Auditorium auditorium, LocalDateTime dateTime) {
        if (event == null || auditorium == null || auditorium.getName() == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        if (eventDao.find(event.getId()) == null) {
            throw new IllegalArgumentException();
        }
        assignedEventsDao.assignAuditorium(event, auditorium, dateTime);
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setAssignedEventsDao(AssignedEventsDao assignedEventsDao) {
        this.assignedEventsDao = assignedEventsDao;
    }
}
