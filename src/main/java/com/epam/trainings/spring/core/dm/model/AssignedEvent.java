package com.epam.trainings.spring.core.dm.model;

import java.time.LocalDateTime;

public class AssignedEvent {

    private Event event;
    private Auditorium auditorium;
    private LocalDateTime dateTime;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssignedEvent that = (AssignedEvent) o;

        if (event != null ? !event.equals(that.event) : that.event != null) {
            return false;
        }
        if (auditorium != null ? !auditorium.equals(that.auditorium) : that.auditorium != null) {
            return false;
        }
        return !(dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null);

    }

    @Override
    public int hashCode() {
        int result = event != null ? event.hashCode() : 0;
        result = 31 * result + (auditorium != null ? auditorium.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
}
