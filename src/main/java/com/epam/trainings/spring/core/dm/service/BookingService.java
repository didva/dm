package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Seat;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    double getTicketPrice(Event event, LocalDateTime dateTime, List<Seat> seats, User user);

    void bookTicket(User user, Ticket ticket);

    List<Ticket> getTicketsForEvent(Event event, LocalDateTime dateTime);

}
