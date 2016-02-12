package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketsDaoInMemoryImpl implements TicketsDao {

    private long count = 0;
    private Map<Long, List<Ticket>> ticketsByUser = new HashMap<>();
    private Map<Long, Ticket> tickets = new HashMap<>();

    @Override
    public Ticket find(long id) {
        return tickets.get(id);
    }

    @Override
    public List<Ticket> findByUserId(long userId) {
        List<Ticket> ticketsForUser = ticketsByUser.get(userId);
        return ticketsForUser == null ? Collections.emptyList() : ticketsForUser;
    }

    @Override
    public void add(User user, Ticket ticket) {
        Long userId = user == null ? null : user.getId();
        List<Ticket> ticketsForUser = ticketsByUser.get(userId);
        if (ticketsForUser == null) {
            ticketsForUser = new ArrayList<>();
            ticketsByUser.put(userId, ticketsForUser);
        }
        ticketsForUser.add(ticket);
        tickets.put(ticket.getId(), ticket);
        ++count;
        ticket.setId(count);
    }

    @Override
    public List<Ticket> findByEvent(long assignedEventId) {
        List<Ticket> matched = new ArrayList<>();
        for (List<Ticket> userTickets : ticketsByUser.values()) {
            matched.addAll(findByEventForUser(userTickets, assignedEventId));
        }
        return matched;
    }

    private List<Ticket> findByEventForUser(List<Ticket> uTickets, long assignedEventId) {
        return uTickets.stream().filter(t -> t.getAssignedEventId() == assignedEventId).collect(Collectors.toList());
    }
}
