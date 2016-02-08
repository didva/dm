package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketDao {

    List<Ticket> findByUserId(long id);

    void add(User user, Ticket ticket);

    List<Ticket> findByEvent(long event, LocalDateTime dateTime);

}
