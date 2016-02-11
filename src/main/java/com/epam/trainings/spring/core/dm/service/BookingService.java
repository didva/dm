package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.annotations.TrackMethodExecutions;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingService {

    @TrackMethodExecutions(description = "Calculate price")
    double getTicketPrice(Event event, LocalDateTime dateTime, Set<Integer> seats, User user);

    @TrackMethodExecutions(description = "Book ticket")
    void bookTicket(User user, Ticket ticket);

    List<Ticket> getTicketsForEvent(Event event, LocalDateTime dateTime);

}
