package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.model.Counter;
import com.epam.trainings.spring.core.dm.service.EventsStatisticService;

public class EventsStatisticServiceImpl implements EventsStatisticService {

    private EventCounterDao eventByNameAccessionsCounterDao;
    private EventCounterDao eventPriceCalculationsCounterDao;
    private EventCounterDao eventTicketsBookingsCounterDao;

    @Override
    public void increaseRequestsByName(long eventId) {
        eventByNameAccessionsCounterDao.increase(eventId);
    }

    @Override
    public void increaseRequestsForPrices(long eventId) {
        eventPriceCalculationsCounterDao.increase(eventId);
    }

    @Override
    public void increaseBookedTimes(long eventId) {
        eventTicketsBookingsCounterDao.increase(eventId);
    }

    @Override
    public Counter<Long> getRequestsByName(long eventId) {
        return eventByNameAccessionsCounterDao.findByEventId(eventId);
    }

    @Override
    public Counter<Long> getRequestsForPrices(long eventId) {
        return eventPriceCalculationsCounterDao.findByEventId(eventId);
    }

    @Override
    public Counter<Long> getBookedTimes(long eventId) {
        return eventTicketsBookingsCounterDao.findByEventId(eventId);
    }

    public void setEventByNameAccessionsCounterDao(EventCounterDao eventByNameAccessionsCounterDao) {
        this.eventByNameAccessionsCounterDao = eventByNameAccessionsCounterDao;
    }

    public void setEventPriceCalculationsCounterDao(EventCounterDao eventPriceCalculationsCounterDao) {
        this.eventPriceCalculationsCounterDao = eventPriceCalculationsCounterDao;
    }

    public void setEventTicketsBookingsCounterDao(EventCounterDao eventTicketsBookingsCounterDao) {
        this.eventTicketsBookingsCounterDao = eventTicketsBookingsCounterDao;
    }
}
