package com.epam.trainings.spring.core.dm.model;

public class Seat {

    private long id;
    private String auditorium;
    private int number;
    private boolean isVip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Seat seat = (Seat) o;

        if (id != seat.id) {
            return false;
        }
        if (number != seat.number) {
            return false;
        }
        if (isVip != seat.isVip) {
            return false;
        }
        return !(auditorium != null ? !auditorium.equals(seat.auditorium) : seat.auditorium != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (auditorium != null ? auditorium.hashCode() : 0);
        result = 31 * result + number;
        result = 31 * result + (isVip ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Seat{" +
               "id=" + id +
               ", auditorium='" + auditorium + '\'' +
               ", number=" + number +
               ", isVip=" + isVip +
               '}';
    }
}
