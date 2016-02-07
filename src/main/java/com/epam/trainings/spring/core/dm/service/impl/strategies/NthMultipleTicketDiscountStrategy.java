package com.epam.trainings.spring.core.dm.service.impl.strategies;

import com.epam.trainings.spring.core.dm.dao.TicketDao;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;
import java.util.List;

public class NthMultipleTicketDiscountStrategy implements DiscountService.DiscountStrategy {

    private double discountPercentage;
    private int ticketsToDiscount;
    private TicketDao ticketDao;

    @Override
    public double getDiscount(User user, Event event, LocalDateTime dateTime) {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        if (user == null) {
            return 0;
        }
        List<Ticket> tickets = ticketDao.findByUserId(user.getId());
        if (tickets.isEmpty() || ticketsToDiscount == 0) {
            return 0;
        }
        return (tickets.size() + 1) % ticketsToDiscount == 0 ? discountPercentage * event.getPrice() : 0;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setTicketsToDiscount(int ticketsToDiscount) {
        if (ticketsToDiscount <= 0) {
            throw new IllegalArgumentException();
        }
        this.ticketsToDiscount = ticketsToDiscount;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
}
