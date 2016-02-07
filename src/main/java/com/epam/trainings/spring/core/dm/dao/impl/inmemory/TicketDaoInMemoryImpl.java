package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.TicketDao;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TicketDaoInMemoryImpl implements TicketDao {

    private Map<Long, List<Ticket>> tickets = new HashMap<>();

    @Override
    public List<Ticket> findByUserId(long id) {
        List<Ticket> ticketsForUser = tickets.get(id);
        return ticketsForUser == null ? Collections.emptyList() : ticketsForUser;
    }

    @Override
    public void add(User user, Ticket ticket) {
        Long userId = user == null ? null : user.getId();
        List<Ticket> ticketsForUser = tickets.get(userId);
        if (ticketsForUser == null) {
            ticketsForUser = new ArrayList<>();
            tickets.put(userId, ticketsForUser);
        }
        ticketsForUser.add(ticket);
    }

    @Override
    public List<Ticket> findByEvent(Event event, LocalDateTime dateTime) {
        List<Ticket> matched = new ArrayList<>();
        for (List<Ticket> userTickets : tickets.values()) {
            matched.addAll(findByEventForUser(userTickets, event, dateTime));
        }
        return matched;
    }

    private List<Ticket> findByEventForUser(List<Ticket> uTickets, Event event, LocalDateTime dateTime) {
        return uTickets.stream().filter(t -> t.getEventId() == event.getId() && t.getEventDateTime().equals(dateTime))
                .collect(Collectors.toList());
    }
}
