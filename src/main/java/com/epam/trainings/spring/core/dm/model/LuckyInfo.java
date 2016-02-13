package com.epam.trainings.spring.core.dm.model;

public class LuckyInfo {

    private long id;
    private long userId;
    private long ticketId;

    public LuckyInfo() {
    }

    public LuckyInfo(long id, long userId, long ticketId) {
        this.id = id;
        this.userId = userId;
        this.ticketId = ticketId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LuckyInfo luckyInfo = (LuckyInfo) o;

        if (userId != luckyInfo.userId) return false;
        return ticketId == luckyInfo.ticketId;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (ticketId ^ (ticketId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "LuckyInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticketId=" + ticketId +
                '}';
    }
}
