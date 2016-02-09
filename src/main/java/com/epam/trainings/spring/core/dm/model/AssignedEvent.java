package com.epam.trainings.spring.core.dm.model;

import java.time.LocalDateTime;

public class AssignedEvent implements Comparable<AssignedEvent> {

    private long id;
    private long eventId;
    private String auditorium;
    private LocalDateTime dateTime;

    public AssignedEvent() {
    }

    public AssignedEvent(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AssignedEvent(long eventId, String auditorium, LocalDateTime dateTime) {
        this.eventId = eventId;
        this.auditorium = auditorium;
        this.dateTime = dateTime;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignedEvent that = (AssignedEvent) o;

        if (id != that.id) return false;
        if (eventId != that.eventId) return false;
        if (auditorium != null ? !auditorium.equals(that.auditorium) : that.auditorium != null) return false;
        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (auditorium != null ? auditorium.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(AssignedEvent o) {
        int result = this.getDateTime().compareTo(o.getDateTime());
        if (result == 0) {
            result = Long.compare(eventId, o.getEventId());
        }
        if (result == 0) {
            if (auditorium == null && o.getAuditorium() != null) {
                return -1;
            } else if (auditorium == null && o.getAuditorium() == null) {
                return 0;
            } else if (auditorium != null && o.getAuditorium() == null) {
                return 1;
            }
            result = auditorium.compareTo(o.getAuditorium());
        }
        if (result == 0) {
            result = Long.compare(id, o.getId());
        }
        return result;
    }

    @Override
    public String toString() {
        return "AssignedEvent{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", auditorium=" + auditorium +
                ", dateTime=" + dateTime +
                '}';
    }
}
