package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.service.EventService;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class EventServiceImpl implements EventService {

    private EventDao eventDao;
    private AssignedEventsDao assignedEventsDao;

    @Override
    @Transactional
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
    @Transactional
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
    public Event getById(long eventId) {
        return eventDao.find(eventId);
    }

    @Override
    public AssignedEvent getAssignedEvent(long eventId, LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException();
        }
        Event event = eventDao.find(eventId);
        if (event == null) {
            throw new IllegalArgumentException();
        }
        return assignedEventsDao.findByEvent(eventId, dateTime);
    }

    @Override
    public List<AssignedEvent> getAllAssignedEvents() {
        return assignedEventsDao.findAll();
    }

    @Override
    public List<AssignedEvent> getAssignedEvents(long eventId) {
        return assignedEventsDao.findByEvent(eventId);
    }

    @Override
    public List<AssignedEvent> getForDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null || !from.isBefore(to)) {
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
        if (assignedEventsDao.findByEvent(event.getId(), dateTime) != null) {
            throw new AlreadyExistsException();
        }
        if (assignedEventsDao.findByAuditorium(auditorium.getName(), dateTime) != null) {
            throw new AlreadyExistsException();
        }
        assignedEventsDao.assignAuditorium(new AssignedEvent(event.getId(), auditorium.getName(), dateTime));
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setAssignedEventsDao(AssignedEventsDao assignedEventsDao) {
        this.assignedEventsDao = assignedEventsDao;
    }
}
