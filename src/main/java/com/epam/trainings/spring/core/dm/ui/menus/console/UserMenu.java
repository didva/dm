package com.epam.trainings.spring.core.dm.ui.menus.console;

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.EventService;
import com.epam.trainings.spring.core.dm.service.UserService;
import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;
import com.epam.trainings.spring.core.dm.ui.Utils;

import java.time.LocalDateTime;

public class UserMenu implements Menu {

    private Menu parent;
    private ConsoleReader consoleReader;
    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;

    @Override
    public void printMenu() {
        System.out.println("1 - Register");
        System.out.println("2 - View assigned events by date range");
        System.out.println("3 - View next assigned events");
        System.out.println("4 - Get ticket price");
        System.out.println("5 - Buy ticket");
        System.out.println("0 - Back");
    }

    @Override
    public Menu select(int num) {
        switch (num) {
            case 1:
                register();
                break;
            case 2:
                getAssignedEventsByDateRange();
                break;
            case 3:
                getNextAssignedEvents();
                break;
            case 4:
                getTicketPrice();
                break;
            case 5:
                //TODO: Buy ticket
                break;
            case 0:
                return parent;
            default:
                System.out.println("Incorrect choice!");
        }
        return this;
    }

    private void getTicketPrice() {
        Event event = Utils.getEventByName(consoleReader, eventService);
        if (event == null) {
            return;
        }
        LocalDateTime dateTime = Utils.readDateTime(consoleReader);
        AssignedEvent assignedEvent = Utils.getAssignedEvent(eventService, event, dateTime);
        if (assignedEvent == null) {
            return;
        }
        //TODO: add selecting user + entering seats information
        bookingService.getTicketPrice(event, dateTime, null, null);
    }


    private void getNextAssignedEvents() {
        System.out.println("Please enter 'to' date time, example 1986-04-08 12:30: ");
        LocalDateTime to = consoleReader.readLocalDateTime();
        System.out.println(eventService.getNextEvents(to));
    }

    private void getAssignedEventsByDateRange() {
        System.out.println("Please enter 'from' date time, example 1986-04-08 12:30: ");
        LocalDateTime from = consoleReader.readLocalDateTime();
        System.out.println("Please enter 'to' date time, example 1986-04-08 12:30: ");
        LocalDateTime to = consoleReader.readLocalDateTime();
        System.out.println(eventService.getForDateRange(from, to));
    }

    private void register() {
        User user = readUser();
        try {
            userService.register(user);
        } catch (AlreadyExistsException e) {
            System.out.println("User with same email already exists: " + userService.getUserByEmail(user.getEmail()));
        }
    }

    private User readUser() {
        User user = new User();
        System.out.println("Please enter user name: ");
        user.setName(consoleReader.readString());
        System.out.println("Please enter birth date: ");
        user.setBirthDate(consoleReader.readLocalDate());
        System.out.println("Please enter email: ");
        user.setEmail(consoleReader.readString());
        return user;
    }

}
