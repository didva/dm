package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.TicketDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Seat;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.DiscountService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private TicketDao ticketDao;
    private AssignedEventsDao assignedEventsDao;
    private AuditoriumService auditoriumService;

    @Override
    public double getTicketPrice(Event event, LocalDateTime dateTime, Set<Seat> seats, User user) {
        if (event == null || dateTime == null || seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (!eventIsAvailable(event.getId(), dateTime) || !seatsAreCorrect(seats)) {
            throw new IllegalArgumentException();
        }
        if (seatsAreBooked(seats, event.getId(), dateTime)) {
            throw new AlreadyExistsException();
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
        if (!eventIsAvailable(ticket.getEventId(), ticket.getEventDateTime()) || !seatsAreCorrect(ticket.getSeats())) {
            throw new IllegalArgumentException();
        }
        if (seatsAreBooked(ticket.getSeats(), ticket.getEventId(), ticket.getEventDateTime())) {
            throw new AlreadyExistsException();
        }
        ticketDao.add(user, ticket);
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event event, LocalDateTime dateTime) {
        if (event == null || dateTime == null) {
            throw new IllegalArgumentException();
        }
        if (!eventIsAvailable(event.getId(), dateTime)) {
            throw new IllegalArgumentException();
        }
        return ticketDao.findByEvent(event.getId(), dateTime);
    }

    private boolean seatsAreBooked(Set<Seat> seats, long eventId, LocalDateTime dateTime) {
        return ticketDao.findByEvent(eventId, dateTime).stream().anyMatch(t -> t.getSeats().removeAll(seats));
    }

    private boolean seatsAreCorrect(Set<Seat> seats) {
        for (Seat seat : seats) {
            Auditorium auditorium = auditoriumService.getAuditorium(seat.getAuditorium());
            if (seat.getNumber() <= 0 || seat.getNumber() > auditorium.getSeatsNumber()) {
                return false;
            }
            boolean containsVipSeat = auditorium.getVipSeats().contains(seat.getNumber());
            if ((containsVipSeat && !seat.isVip()) || (!containsVipSeat && seat.isVip())) {
                return false;
            }
        }
        return true;
    }

    private boolean eventIsAvailable(long eventId, LocalDateTime dateTime) {
        return assignedEventsDao.findByEvent(eventId, dateTime) != null;
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

    public void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }
}
