package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class AssignedEventsDaoInMemoryImpl implements AssignedEventsDao {

    private TreeSet<AssignedEvent> events = new TreeSet<>((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));

    @Override
    public List<AssignedEvent> findByRange(LocalDateTime from, LocalDateTime to) {
        return new ArrayList<>(events.subSet(new AssignedEvent(from), new AssignedEvent(to)));
    }

    @Override
    public void assignAuditorium(AssignedEvent assignedEvent) {
        events.add(assignedEvent);
    }
}
