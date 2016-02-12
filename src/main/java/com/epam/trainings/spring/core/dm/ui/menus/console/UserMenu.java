package com.epam.trainings.spring.core.dm.ui.menus.console;

import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.EventService;
import com.epam.trainings.spring.core.dm.service.UserService;
import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;
import com.epam.trainings.spring.core.dm.ui.Utils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserMenu implements Menu {

    private Menu parent;
    private ConsoleReader consoleReader;
    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;
    private AuditoriumService auditoriumService;

    @Override
    public void printMenu() {
        System.out.println("1 - Register");
        System.out.println("2 - View assigned events by date range");
        System.out.println("3 - View next assigned events");
        System.out.println("4 - Get ticket price");
        System.out.println("5 - Book ticket");
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
                bookTicket();
                break;
            case 0:
                return parent;
            default:
                System.out.println("Incorrect choice!");
        }
        return this;
    }

    private void bookTicket() {
        Event event = Utils.getEventByName(consoleReader, eventService);
        if (event == null) {
            return;
        }
        LocalDateTime dateTime = Utils.readDateTime(consoleReader);
        AssignedEvent assignedEvent = Utils.getAssignedEvent(eventService, event, dateTime);
        if (assignedEvent == null) {
            return;
        }
        User user = null;
        if (!isUserAnonymous()) {
            user = getUserByEmail();
            if (user == null) {
                return;
            }
        }
        int seatsNumber = auditoriumService.getAuditorium(assignedEvent.getAuditorium()).getSeatsNumber();
        Set<Integer> seats = readSeatsInformation(seatsNumber);
        try {
            bookingService.bookTicket(assignedEvent, seats, user);
        } catch (AlreadyExistsException e) {
            System.out.println("Sorry, this seats already booked!");
        }

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
        User user = null;
        if (!isUserAnonymous()) {
            user = getUserByEmail();
            if (user == null) {
                return;
            }
        }
        int seatsNumber = auditoriumService.getAuditorium(assignedEvent.getAuditorium()).getSeatsNumber();
        Set<Integer> seats = readSeatsInformation(seatsNumber);
        System.out.println("Price is: " + bookingService.getTicketPrice(assignedEvent, seats, user));
    }

    private Set<Integer> readSeatsInformation(int seatsNumber) {
        Set<Integer> seats = new HashSet<>();
        do {
            System.out.println("Please enter seat number: ");
            int seat = consoleReader.readInt();
            if (seat <= 0 || seat > seatsNumber) {
                System.out.println("Incorrect seat for this auditorium, seats number = " + seatsNumber);
                System.out.println("Enter any other seat (true/false)?");
                continue;
            }
            seats.add(seat);
            System.out.println("Enter any other seat (true/false)?");
        } while (consoleReader.readBoolean());
        return seats;
    }

    private boolean isUserAnonymous() {
        System.out.println("Is user anonymous (true/false)?");
        return consoleReader.readBoolean();
    }

    private User getUserByEmail() {
        System.out.println("Please enter user email: ");
        String email = consoleReader.readString();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            System.out.println("No user was found!");
            System.out.println("Here are all users: ");
            System.out.println(userService.getAll());
        }
        return user;
    }

    private void getNextAssignedEvents() {
        System.out.println("Please enter 'to' date time, example 1986-04-08 12:30: ");
        LocalDateTime to = consoleReader.readLocalDateTime();
        if (!to.isAfter(LocalDateTime.now())) {
            System.out.println("Incorrect 'to' date, it should be after 'now'!");
            return;
        }
        System.out.println(eventService.getNextEvents(to));
    }

    private void getAssignedEventsByDateRange() {
        System.out.println("Please enter 'from' date time, example 1986-04-08 12:30: ");
        LocalDateTime from = consoleReader.readLocalDateTime();
        System.out.println("Please enter 'to' date time, example 1986-04-08 12:30: ");
        LocalDateTime to = consoleReader.readLocalDateTime();
        if (!to.isAfter(from)) {
            System.out.println("Incorrect 'to' date, it should be after 'from'!");
            return;
        }
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

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public void setConsoleReader(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }
}
