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

    static Ticket createTicket(long id, long assignedEventId, Long userId, List<Integer> seats, double finalPrice) {
        def ticket = Ticket.newInstance();
        ticket.id = id;
        ticket.assignedEventId = assignedEventId;
        ticket.userId = userId;
        ticket.seats = seats as Set;
        ticket.finalPrice = finalPrice;
        ticket
    }

    static Event createEvent(String name, double price, Rating rating) {
        def event = Event.newInstance();
        event.name = name;
        event.price = price;
        event.rating = rating
        event
    }

    static AssignedEvent createAssignedEvent(Long eventId, String auditorium, LocalDateTime dateTime) {
        def assignedEvent = AssignedEvent.newInstance();
        assignedEvent.eventId = eventId;
        assignedEvent.auditorium = auditorium
        assignedEvent.dateTime = dateTime
        assignedEvent
    }

    static Auditorium createAuditorium(String name, int seats, List<Integer> vip) {
        def auditorium = Auditorium.newInstance()
        auditorium.name = name
        auditorium.seatsNumber = seats
        auditorium.vipSeats = vip as Set
        auditorium
    }

}
