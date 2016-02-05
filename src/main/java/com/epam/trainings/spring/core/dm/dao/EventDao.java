package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDao {

    void add(Event event);

    void delete(long id);

    Event find(long id);

    Event findByName(String name);

    List<Event> findAll();

}
