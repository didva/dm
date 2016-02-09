package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.*;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private TicketsDao ticketsDao;
    private AssignedEventsDao assignedEventsDao;
    private AuditoriumService auditoriumService;

    @Override
    public double getTicketPrice(Event event, LocalDateTime dateTime, Set<Integer> seats, User user) {
        if (event == null || dateTime == null || seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException();
        }

        AssignedEvent assignedEvent = assignedEventsDao.findByEvent(event.getId(), dateTime);
        if (assignedEvent == null) {
            throw new IllegalArgumentException();
        }
        Auditorium auditorium = auditoriumService.getAuditorium(assignedEvent.getAuditorium());
        if (auditorium == null) {
            throw new IllegalStateException();
        }
        if (!seatsAreCorrect(auditorium, seats)) {
            throw new IllegalArgumentException();
        }
        if (seatsAreBooked(seats, assignedEvent.getId())) {
            throw new AlreadyExistsException();
        }

        double totalPrice = 0, discount = discountService.getDiscount(user, event, dateTime),
                seatPrice = event.getPrice() - discount;
        Set<Integer> vipSeats = auditorium.getVipSeats();
        for (Integer seat : seats) {
            totalPrice += vipSeats.contains(seat) ? seatPrice * 2 : seatPrice;
        }
        return totalPrice * event.getRating().getMultiplier();
    }

    @Override
    public void bookTicket(User user, Ticket ticket) {
        if (ticket == null || ticket.getSeats() == null || ticket.getSeats().isEmpty()) {
            throw new IllegalArgumentException();
        }

        AssignedEvent assignedEvent = assignedEventsDao.find(ticket.getAssignedEventId());
        if (assignedEvent == null) {
            throw new IllegalArgumentException();
        }
        Auditorium auditorium = auditoriumService.getAuditorium(assignedEvent.getAuditorium());
        if (auditorium == null) {
            throw new IllegalStateException();
        }
        if (!seatsAreCorrect(auditorium, ticket.getSeats())) {
            throw new IllegalArgumentException();
        }
        if (seatsAreBooked(ticket.getSeats(), assignedEvent.getId())) {
            throw new AlreadyExistsException();
        }

        ticketsDao.add(user, ticket);
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event event, LocalDateTime dateTime) {
        if (event == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        AssignedEvent assignedEvent = assignedEventsDao.findByEvent(event.getId(), dateTime);
        if (assignedEvent == null) {
            throw new IllegalArgumentException();
        }
        return ticketsDao.findByEvent(assignedEvent.getId());
    }

    private boolean seatsAreBooked(Set<Integer> seats, long assignedEventId) {
        return ticketsDao.findByEvent(assignedEventId).stream().anyMatch(t -> t.getSeats().removeAll(seats));
    }

    private boolean seatsAreCorrect(Auditorium auditorium, Set<Integer> seats) {
        for (Integer seat : seats) {
            if (seat <= 0 || seat > auditorium.getSeatsNumber()) {
                return false;
            }
        }
        return true;
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setTicketsDao(TicketsDao ticketsDao) {
        this.ticketsDao = ticketsDao;
    }

    public void setAssignedEventsDao(AssignedEventsDao assignedEventsDao) {
        this.assignedEventsDao = assignedEventsDao;
    }

    public void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }
}
