package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Counter;
import com.epam.trainings.spring.core.dm.model.LuckyInfo;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.service.EventService;
import com.epam.trainings.spring.core.dm.service.StatisticService;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class StatisticServiceImpl implements StatisticService {

    private EventCounterDao eventByNameAccessionsCounterDao;
    private EventCounterDao eventPriceCalculationsCounterDao;
    private EventCounterDao eventTicketsBookingsCounterDao;
    private DiscountCounterDao discountCounterDao;
    private LuckyDao luckyDao;
    private EventService eventService;
    private TicketsDao ticketsDao;

    @Override
    @Transactional
    public void increaseDiscounts(String discountName, long userId) {
        discountCounterDao.increase(discountName, userId);
    }

    @Override
    @Transactional
    public void increaseRequestsByName(long eventId) {
        eventByNameAccessionsCounterDao.increase(eventId);
    }

    @Override
    @Transactional
    public void increaseRequestsForPrices(long eventId) {
        eventPriceCalculationsCounterDao.increase(eventId);
    }

    @Override
    @Transactional
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
    @Transactional
    public void userIsLucky(long userId, long ticketId) {
        luckyDao.register(ticketId, userId);
    }

    @Override
    public List<LuckyInfo> getLuckyInfoByUserId(long userId) {
        return luckyDao.findByUserId(userId);
    }

    @Override
    public List<LuckyInfo> getLuckyInfoByEventId(long eventId) {
        List<LuckyInfo> luckyInfoList = new ArrayList<>();
        for (AssignedEvent assignedEvent : eventService.getAssignedEvents(eventId)) {
            List<Ticket> tickets = ticketsDao.findByEvent(assignedEvent.getId());
            for (Ticket ticket : tickets) {
                LuckyInfo luckyInfo = luckyDao.findByTicketId(ticket.getId());
                if (luckyInfo != null) {
                    luckyInfoList.add(luckyInfo);
                }
            }
        }
        return luckyInfoList;
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

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setTicketsDao(TicketsDao ticketsDao) {
        this.ticketsDao = ticketsDao;
    }
}
