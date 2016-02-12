package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import static java.util.stream.Collectors.toList;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.LuckyInfo;
import com.epam.trainings.spring.core.dm.model.Ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuckyDaoInMemoryImpl implements LuckyDao {

    private long counter = 0;
    private Map<Long, LuckyInfo> luckyInfoByTicket = new HashMap<>();

    private TicketsDao ticketsDao;
    private AssignedEventsDao assignedEventsDao;

    @Override
    public void register(long ticketId, long userId) {
        if (luckyInfoByTicket.containsKey(ticketId)) {
            throw new AlreadyExistsException();
        }
        counter++;
        luckyInfoByTicket.put(ticketId, new LuckyInfo(counter, userId, ticketId));
    }

    @Override
    public List<LuckyInfo> findByEventId(long eventId) {
        List<LuckyInfo> resultList = new ArrayList<>();
        for (Long ticketId : luckyInfoByTicket.keySet()) {
            Ticket ticket = ticketsDao.find(ticketId);
            AssignedEvent assignedEvent = assignedEventsDao.find(ticket.getAssignedEventId());
            if (assignedEvent.getEventId() == eventId) {
                resultList.add(luckyInfoByTicket.get(ticketId));
            }
        }
        return resultList;
    }

    @Override
    public List<LuckyInfo> findByUserId(long userId) {
        return luckyInfoByTicket.values().stream().filter(l -> l.getUserId() == userId).collect(toList());
    }

    public void setTicketsDao(TicketsDao ticketsDao) {
        this.ticketsDao = ticketsDao;
    }

    public void setAssignedEventsDao(AssignedEventsDao assignedEventsDao) {
        this.assignedEventsDao = assignedEventsDao;
    }
}
