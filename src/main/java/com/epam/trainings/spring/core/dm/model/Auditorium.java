package com.epam.trainings.spring.core.dm.model;

import java.util.HashSet;
import java.util.Set;

public class Auditorium {

    private String name;
    private int seatsNumber;
    private Set<Integer> vipSeats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public Set<Integer> getVipSeats() {
        return new HashSet<>(vipSeats);
    }

    public void setVipSeats(Set<Integer> vipSeats) {
        this.vipSeats = vipSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Auditorium that = (Auditorium) o;

        if (seatsNumber != that.seatsNumber) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return !(vipSeats != null ? !vipSeats.equals(that.vipSeats) : that.vipSeats != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + seatsNumber;
        result = 31 * result + (vipSeats != null ? vipSeats.hashCode() : 0);
        return result;
    }
}
