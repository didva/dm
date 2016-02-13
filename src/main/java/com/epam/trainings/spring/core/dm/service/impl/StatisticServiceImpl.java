package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.model.Counter;
import com.epam.trainings.spring.core.dm.model.LuckyInfo;
import com.epam.trainings.spring.core.dm.service.StatisticService;

import java.util.List;

public class StatisticServiceImpl implements StatisticService {

    private EventCounterDao eventByNameAccessionsCounterDao;
    private EventCounterDao eventPriceCalculationsCounterDao;
    private EventCounterDao eventTicketsBookingsCounterDao;
    private DiscountCounterDao discountCounterDao;
    private LuckyDao luckyDao;

    @Override
    public void increaseDiscounts(String discountName, long userId) {
        discountCounterDao.increase(discountName, userId);
    }

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

    @Override
    public List<Counter<String>> getAllDiscounts() {
        return discountCounterDao.findAll();
    }

    @Override
    public List<Counter<String>> getDiscountsByUser(long userId) {
        return discountCounterDao.findByUserId(userId);
    }

    @Override
    public void userIsLucky(long userId, long ticketId) {
        luckyDao.register(ticketId, userId);
    }

    @Override
    public List<LuckyInfo> getLuckyInfoByUserId(long userId) {
        return luckyDao.findByUserId(userId);
    }

    @Override
    public List<LuckyInfo> getLuckyInfoByEventId(long eventId) {
        return luckyDao.findByEventId(eventId);
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

    public void setDiscountCounterDao(DiscountCounterDao discountCounterDao) {
        this.discountCounterDao = discountCounterDao;
    }

    public void setLuckyDao(LuckyDao luckyDao) {
        this.luckyDao = luckyDao;
    }
}
