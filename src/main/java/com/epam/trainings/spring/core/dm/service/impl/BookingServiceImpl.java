package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.*;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.DiscountService;
import com.epam.trainings.spring.core.dm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private TicketsDao ticketsDao;
    private AuditoriumService auditoriumService;
    private EventService eventService;

    @Override
    public double getTicketPrice(AssignedEvent assignedEvent, Set<Integer> seats, User user) {
        if (assignedEvent == null || seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Event event = eventService.getById(assignedEvent.getEventId());
        if (event == null) {
            throw new IllegalStateException();
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

        return checkPrice(event, assignedEvent.getDateTime(), seats, user, auditorium);
    }

    @Override
    public void bookTicket(AssignedEvent assignedEvent, Set<Integer> seats, User user) {
        if (assignedEvent == null || seats == null || seats.isEmpty()) {
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
        Event event = eventService.getById(assignedEvent.getEventId());
        if (event == null) {
            throw new IllegalStateException();
        }
        Ticket ticket = getTicket(event, assignedEvent, user, seats, auditorium);
        ticketsDao.add(user, ticket);
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event event, LocalDateTime dateTime) {
        if (event == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        AssignedEvent assignedEvent = eventService.getAssignedEvent(event.getId(), dateTime);
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

    private Ticket getTicket(Event event, AssignedEvent assignedEvent, User user, Set<Integer> seats, Auditorium auditorium) {
        Ticket ticket = new Ticket();
        ticket.setAssignedEventId(assignedEvent.getId());
        assignedEvent.getAuditorium();
        ticket.setFinalPrice(calculatePrice(event, assignedEvent.getDateTime(), seats, user, auditorium));
        ticket.setSeats(seats);
        ticket.setUserId(user == null ? null : user.getId());
        return ticket;
    }

    private double checkPrice(Event event, LocalDateTime dateTime, Set<Integer> seats, User user, Auditorium auditorium) {
        double discount = discountService.checkDiscount(user, event, dateTime);
        return getPrice(event, seats, auditorium, discount);
    }

    private double getPrice(Event event, Set<Integer> seats, Auditorium auditorium, double discount) {
        double totalPrice = 0, seatPrice = event.getPrice() - discount;
        Set<Integer> vipSeats = auditorium.getVipSeats();
        for (Integer seat : seats) {
            totalPrice += vipSeats.contains(seat) ? seatPrice * 2 : seatPrice;
        }
        return totalPrice * event.getRating().getMultiplier();
    }

    private double calculatePrice(Event event, LocalDateTime dateTime, Set<Integer> seats, User user, Auditorium auditorium) {
        double discount = discountService.getDiscount(user, event, dateTime);
        return getPrice(event, seats, auditorium, discount);
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setTicketsDao(TicketsDao ticketsDao) {
        this.ticketsDao = ticketsDao;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }
}
