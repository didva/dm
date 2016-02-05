package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Ticket;

import java.util.List;

public interface TicketDao {

    List<Ticket> findByUserId(long id);

}
