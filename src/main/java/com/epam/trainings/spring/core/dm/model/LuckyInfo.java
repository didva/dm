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
}
