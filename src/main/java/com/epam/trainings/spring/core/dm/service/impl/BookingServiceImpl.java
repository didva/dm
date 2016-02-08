package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.TicketDao;
import com.epam.trainings.spring.core.dm.model.*;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private TicketDao ticketDao;
    private AssignedEventsDao assignedEventsDao;

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
        AssignedEvent assignedEvent = assignedEventsDao.findByEvent(ticket.getEventId(), ticket.getEventDateTime());
        if (assignedEvent == null) {
            throw new IllegalArgumentException();
        }
        boolean alreadyBooked = ticketDao.findByEvent(assignedEvent.getEvent(), assignedEvent.getDateTime()).stream()
                .anyMatch(t -> t.getSeats().removeAll(ticket.getSeats()));
        if (alreadyBooked) {
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

    public void setAssignedEventsDao(AssignedEventsDao assignedEventsDao) {
        this.assignedEventsDao = assignedEventsDao;
    }
}
