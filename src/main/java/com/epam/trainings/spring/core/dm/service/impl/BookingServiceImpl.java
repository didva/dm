package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.TicketDao;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Seat;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private TicketDao ticketDao;

    @Override
    public double getTicketPrice(Event event, LocalDateTime dateTime, List<Seat> seats, User user) {
        if (event == null || dateTime == null || seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException();
        }
        double totalPrice = 0,
                discount = discountService.getDiscount(user, event, dateTime),
                seatPrice = event.getPrice() - discount;
        for (Seat seat : seats) {
            totalPrice += seat.isVip() ? seatPrice * 2 : seatPrice;
        }
        return totalPrice * event.getRating().getMultiplier();
    }

    @Override
    public void bookTicket(User user, Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException();
        }
        ticketDao.add(user, ticket);
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event event, LocalDateTime dateTime) {
        if (event == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        return ticketDao.findByEvent(event, dateTime);
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
}
