package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.dao.*;
import com.epam.trainings.spring.core.dm.service.*;
import com.epam.trainings.spring.core.dm.service.impl.*;
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy;
import com.epam.trainings.spring.core.dm.service.impl.strategies.NthMultipleTicketDiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Arrays;

@Configuration
@PropertySource("classpath:config.properties")
public class AppServiceConfig {

    @Value("${discount.birthday.discountPercentage}")
    private double birthdayDiscount;
    @Value("${discount.nthTicket.discountPercentage}")
    private double nthTicketDiscount;
    @Value("${discount.nthTicket.ticketsToDiscount}")
    private int ticketToDiscount;

    @Autowired
    private UserDao userDao;
    @Autowired
    private AuditoriumDao auditoriumDao;
    @Autowired
    private TicketsDao ticketsDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private AssignedEventsDao assignedEventsDao;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public AuditoriumService auditoriumService() {
        AuditoriumServiceImpl auditoriumService = new AuditoriumServiceImpl();
        auditoriumService.setAuditoriumDao(auditoriumDao);
        return auditoriumService;
    }

    @Bean
    public DiscountService discountService() {
        DiscountServiceImpl discountService = new DiscountServiceImpl();
        discountService.setStrategies(Arrays.asList(createBirthdayDiscount(), createNthTicketDiscount()));
        return discountService;
    }

    @Bean
    public BookingService bookingService() {
        BookingServiceImpl bookingService = new BookingServiceImpl();
        bookingService.setTicketsDao(ticketsDao);
        bookingService.setAssignedEventsDao(assignedEventsDao);
        bookingService.setAuditoriumService(auditoriumService());
        bookingService.setDiscountService(discountService());
        return bookingService;
    }

    @Bean
    public EventService eventService() {
        EventServiceImpl eventService = new EventServiceImpl();
        eventService.setEventDao(eventDao);
        eventService.setAssignedEventsDao(assignedEventsDao);
        return eventService;
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao);
        userService.setTicketsDao(ticketsDao);
        return userService;
    }

    private DiscountService.DiscountStrategy createBirthdayDiscount() {
        BirthdayDiscountStrategy birthday = new BirthdayDiscountStrategy();
        birthday.setDiscountPercentage(birthdayDiscount);
        return birthday;
    }

    private DiscountService.DiscountStrategy createNthTicketDiscount() {
        NthMultipleTicketDiscountStrategy nthMultipleTicketDiscountStrategy = new NthMultipleTicketDiscountStrategy();
        nthMultipleTicketDiscountStrategy.setTicketsDao(ticketsDao);
        nthMultipleTicketDiscountStrategy.setDiscountPercentage(nthTicketDiscount);
        nthMultipleTicketDiscountStrategy.setTicketsToDiscount(ticketToDiscount);
        return nthMultipleTicketDiscountStrategy;
    }
}