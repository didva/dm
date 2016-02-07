package com.epam.trainings.spring.core.dm

import com.epam.trainings.spring.core.dm.model.*

import java.time.LocalDate
import java.time.LocalDateTime

class Utils {

    static User createUser(long id, String name, String email, LocalDate birthDate) {
        def user = User.newInstance();
        user.id = id;
        user.name = name;
        user.email = email;
        user.birthDate = birthDate
        user
    }

    static Ticket createTicket(long id) {
        def ticket = Ticket.newInstance();
        ticket.id = id;
        ticket
    }

    static Event createEvent(String name, double price, Rating rating) {
        def event = Event.newInstance();
        event.name = name;
        event.price = price;
        event.rating = rating
        event
    }

    static AssignedEvent createAssignedEvent(Event event, Auditorium auditorium, LocalDateTime dateTime) {
        def assignedEvent = AssignedEvent.newInstance();
        assignedEvent.event = event;
        assignedEvent.auditorium = auditorium
        assignedEvent.dateTime = dateTime
        assignedEvent
    }

    static Auditorium createAuditorium(String name) {
        def auditorium = Auditorium.newInstance();
        auditorium.name = name;
        auditorium
    }

    static Seat createSeat(long id, boolean vip) {
        def seat = Seat.newInstance();
        seat.id = id;
        seat.vip = vip
        seat
    }

}
