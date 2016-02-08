package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDaoInMemoryImpl implements EventDao {

    private Map<Long, Event> eventMap = new HashMap<>();

    @Override
    public void add(Event event) {
        event.setId(eventMap.size() + 1);
        eventMap.put(event.getId(), event);
    }

    @Override
    public void delete(long id) {
        eventMap.remove(id);
    }

    @Override
    public Event find(long id) {
        return eventMap.get(id);
    }

    @Override
    public Event findByName(String name) {
        return eventMap.values().stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(eventMap.values());
    }
}
