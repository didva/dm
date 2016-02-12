package com.epam.trainings.spring.core.dm.model;

import java.util.HashSet;
import java.util.Set;

public class Ticket {

    private long id;
    private long assignedEventId;
    private Long userId;
    private Set<Integer> seats;
    private double finalPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getAssignedEventId() {
        return assignedEventId;
    }

    public void setAssignedEventId(long eventId) {
        this.assignedEventId = eventId;
    }

    public Set<Integer> getSeats() {
        return new HashSet<>(seats);
    }

    public void setSeats(Set<Integer> seats) {
        this.seats = seats;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ticket ticket = (Ticket) o;

        if (assignedEventId != ticket.assignedEventId) {
            return false;
        }
        if (Double.compare(ticket.finalPrice, finalPrice) != 0) {
            return false;
        }
        if (userId != null ? !userId.equals(ticket.userId) : ticket.userId != null) {
            return false;
        }
        return !(seats != null ? !seats.equals(ticket.seats) : ticket.seats != null);

    }

    @Override
    public int hashCode() {
        int result= 31 + (int) (assignedEventId ^ (assignedEventId >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
        long temp = Double.doubleToLongBits(finalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", eventId=" + assignedEventId +
                ", userId=" + userId +
                ", seats=" + seats +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
