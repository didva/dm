package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class TicketsDaoInMemoryImpl implements TicketsDao {

    private long count = 0;
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
        ++count;
        ticket.setId(count);
    }

    @Override
    public List<Ticket> findByEvent(long assignedEventId) {
        List<Ticket> matched = new ArrayList<>();
        for (List<Ticket> userTickets : tickets.values()) {
            matched.addAll(findByEventForUser(userTickets, assignedEventId));
        }
        return matched;
    }

    private List<Ticket> findByEventForUser(List<Ticket> uTickets, long assignedEventId) {
        return uTickets.stream().filter(t -> t.getAssignedEventId() == assignedEventId).collect(Collectors.toList());
    }
}
