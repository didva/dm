package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.model.Counter;

import java.util.HashMap;
import java.util.Map;

public class GeneralEventCounterDaoInMemoryImpl implements EventCounterDao {

    private Map<Long, Integer> statistics = new HashMap<>();

    @Override
    public void increase(long eventId) {
        statistics.compute(eventId, (eId, counter) -> counter == null ? 1 : counter + 1);
    }

    @Override
    public Counter<Long> findByEventId(long eventId) {
        return new Counter<>(eventId, statistics.getOrDefault(eventId, 0));
    }
}
