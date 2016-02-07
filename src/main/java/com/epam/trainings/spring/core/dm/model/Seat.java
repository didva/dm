package com.epam.trainings.spring.core.dm.model;

public class Seat {

    private long id;
    private long auditoriumId;
    private int number;
    private boolean isVip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuditoriumId() {
        return auditoriumId;
    }

    public void setAuditoriumId(long auditoriumId) {
        this.auditoriumId = auditoriumId;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        if (id != seat.id) return false;
        if (auditoriumId != seat.auditoriumId) return false;
        if (number != seat.number) return false;
        return isVip == seat.isVip;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (auditoriumId ^ (auditoriumId >>> 32));
        result = 31 * result + number;
        result = 31 * result + (isVip ? 1 : 0);
        return result;
    }
}
