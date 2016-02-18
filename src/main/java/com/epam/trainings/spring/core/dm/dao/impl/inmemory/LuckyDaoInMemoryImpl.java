package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import static java.util.stream.Collectors.toList;

import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.LuckyInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuckyDaoInMemoryImpl implements LuckyDao {

    private long counter = 0;
    private Map<Long, LuckyInfo> luckyInfoByTicket = new HashMap<>();

    @Override
    public void register(long ticketId, long userId) {
        if (luckyInfoByTicket.containsKey(ticketId)) {
            throw new AlreadyExistsException();
        }
        counter++;
        luckyInfoByTicket.put(ticketId, new LuckyInfo(counter, userId, ticketId));
    }

    @Override
    public LuckyInfo findByTicketId(long ticketId) {
        return luckyInfoByTicket.get(ticketId);
    }

    @Override
    public List<LuckyInfo> findByUserId(long userId) {
        return luckyInfoByTicket.values().stream().filter(l -> l.getUserId() == userId).collect(toList());
    }

}
