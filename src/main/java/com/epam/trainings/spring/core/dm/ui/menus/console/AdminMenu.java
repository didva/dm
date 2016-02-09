package com.epam.trainings.spring.core.dm.ui.menus.console;

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.EventService;
import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;
import com.epam.trainings.spring.core.dm.ui.Utils;

import java.time.LocalDateTime;

public class AdminMenu implements Menu {

    private EventService eventService;
    private AuditoriumService auditoriumService;
    private BookingService bookingService;
    private Menu parent;
    private ConsoleReader consoleReader;

    @Override
    public void printMenu() {
        System.out.println("1 - New event");
        System.out.println("2 - Assign event to auditorium");
        System.out.println("3 - View purchased tickets");
        System.out.println("0 - Back");
    }

    @Override
    public Menu select(int num) {
        switch (num) {
            case 1:
                createEvent();
                break;
            case 2:
                assignEvent();
                break;
            case 3:
                viewPurchasedTickets();
                break;
            case 0:
                return parent;
            default:
                System.out.println("Incorrect choice!");
        }
        return this;
    }

    private void viewPurchasedTickets() {
        Event event = Utils.getEventByName(consoleReader, eventService);
        if (event == null) {
            return;
        }
        LocalDateTime dateTime = Utils.readDateTime(consoleReader);
        AssignedEvent assignedEvent = Utils.getAssignedEvent(eventService, event, dateTime);
        if (assignedEvent == null) {
            return;
        }
        System.out.println(bookingService.getTicketsForEvent(event, dateTime));
    }

    private void assignEvent() {
        Event event = Utils.getEventByName(consoleReader, eventService);
        if (event == null) {
            return;
        }
        Auditorium auditorium = getAuditoriumByName();
        if (auditorium == null) {
            return;
        }
        LocalDateTime dateTime = Utils.readDateTime(consoleReader);
        try {
            eventService.assignAuditorium(event, auditorium, dateTime);
        } catch (AlreadyExistsException e) {
            System.out.println("Already this time is already assigned for this event or this auditory!");
            System.out.println("Here are all assigned events: ");
            System.out.println(eventService.getAllAssignedEvents());
        }
    }

    private Auditorium getAuditoriumByName() {
        System.out.println("Please enter auditorium name: ");
        String auditoriumName = consoleReader.readString();
        Auditorium auditorium = auditoriumService.getAuditorium(auditoriumName);
        if (auditorium == null) {
            System.out.println("Auditorium not found!");
            System.out.println("Here are all auditoriums: ");
            System.out.println(auditoriumService.getAuditoriums());
        }
        return auditorium;
    }

    private void createEvent() {
        Event event = readEvent();
        try {
            eventService.create(event);
        } catch (AlreadyExistsException e) {
            System.out.println("Event with such name already exists: " + eventService.getByName(event.getName()));
        }
    }

    private Event readEvent() {
        Event event = new Event();
        System.out.println("Please enter event name: ");
        event.setName(consoleReader.readString());
        System.out.println("Please enter event base price: ");
        event.setPrice(consoleReader.readDouble());
        System.out.println("Please enter event rating: ");
        event.setRating(consoleReader.readRating());
        return event;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public void setConsoleReader(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
