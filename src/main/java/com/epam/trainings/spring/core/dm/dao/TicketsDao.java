package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.util.List;

public interface TicketsDao {

    Ticket find(long id);

    List<Ticket> findByUserId(long userId);

    void add(User user, Ticket ticket);

    List<Ticket> findByEvent(long assignedEventId);

}
