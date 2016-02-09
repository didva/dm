package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class AssignedEventsDaoInMemoryImpl implements AssignedEventsDao {

    private TreeSet<AssignedEvent> events = new TreeSet<>();

    @Override
    public AssignedEvent find(long id) {
        return events.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<AssignedEvent> findAll() {
        return new ArrayList<>(events);
    }

    @Override
    public List<AssignedEvent> findByRange(LocalDateTime from, LocalDateTime to) {
        return new ArrayList<>(events.subSet(new AssignedEvent(from), new AssignedEvent(to)));
    }

    @Override
    public void assignAuditorium(AssignedEvent assignedEvent) {
        assignedEvent.setId(events.size() + 1);
        events.add(assignedEvent);
    }

    @Override
    public AssignedEvent findByEvent(long eventId, LocalDateTime dateTime) {
        return events.stream().filter(e -> e.getEventId() == eventId && e.getDateTime().equals(dateTime)).findFirst()
                .orElse(null);
    }

    @Override
    public AssignedEvent findByAuditorium(String auditorium, LocalDateTime dateTime) {
        return events.stream().filter(e -> e.getAuditorium().equals(auditorium) && e.getDateTime().equals(dateTime))
                .findFirst().orElse(null);
    }
}
