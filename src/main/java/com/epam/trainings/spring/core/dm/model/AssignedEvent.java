package com.epam.trainings.spring.core.dm.model;

import java.time.LocalDateTime;

public class AssignedEvent implements Comparable<AssignedEvent> {

    private Event event;
    private Auditorium auditorium;
    private LocalDateTime dateTime;

    public AssignedEvent() {
    }

    public AssignedEvent(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AssignedEvent(Event event, Auditorium auditorium, LocalDateTime dateTime) {
        this.event = event;
        this.auditorium = auditorium;
        this.dateTime = dateTime;
    }

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

    @Override
    public int compareTo(AssignedEvent o) {
        int result = this.getDateTime().compareTo(o.getDateTime());
        if (result == 0) {
            if (event == null && o.getEvent() != null) {
                return -1;
            } else if (event == null && o.getEvent() == null) {
                return 0;
            } else if (event != null && o.getEvent() == null) {
                return 1;
            }
            result = Long.compare(event.getId(), o.getEvent().getId());
        }
        if (result == 0) {
            if (auditorium == null && o.getAuditorium() != null) {
                return -1;
            } else if (auditorium == null && o.getAuditorium() == null) {
                return 0;
            } else if (auditorium != null && o.getAuditorium() == null) {
                return 1;
            }
            result = auditorium.getName().compareTo(o.getAuditorium().getName());
        }
        return result;
    }

    @Override
    public String toString() {
        return "AssignedEvent{" +
               "event=" + event +
               ", auditorium=" + auditorium +
               ", dateTime=" + dateTime +
               '}';
    }
}
