package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Counter;

public interface EventsStatisticService {

    void increaseRequestsByName(long eventId);

    void increaseRequestsForPrices(long eventId);

    void increaseBookedTimes(long eventId);

    Counter<Long> getRequestsByName(long eventId);

    Counter<Long> getRequestsForPrices(long eventId);

    Counter<Long> getBookedTimes(long eventId);

}
