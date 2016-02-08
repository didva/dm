package com.epam.trainings.spring.core.dm.ui;

import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.service.EventService;

import java.time.LocalDateTime;

public final class Utils {

    private Utils() {
    }

    public static Event getEventByName(ConsoleReader reader, EventService eventService) {
        System.out.println("Please enter event name: ");
        String eventName = reader.readString();
        Event event = eventService.getByName(eventName);
        if (event == null) {
            System.out.println("Event not found!");
            System.out.println("Here are all events: ");
            System.out.println(eventService.getAll());
        }
        return event;
    }

    public static AssignedEvent getAssignedEvent(EventService eventService, Event event, LocalDateTime dateTime) {
        AssignedEvent assignedEvent = eventService.getAssignedEvent(event.getId(), dateTime);
        if (assignedEvent == null) {
            System.out.println("No assigned event found!");
            System.out.println("Here are all assigned events:");
            System.out.println(eventService.getAllAssignedEvents());
        }
        return assignedEvent;
    }

    public static LocalDateTime readDateTime(ConsoleReader reader) {
        System.out.println("Please enter date and time for event, example: 1986-04-08 12:30");
        return reader.readLocalDateTime();
    }

}
