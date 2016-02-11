package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Counter;

public interface EventCounterDao {

    void increase(long eventId);

    Counter<Long> findByItemId(long eventId);

}
