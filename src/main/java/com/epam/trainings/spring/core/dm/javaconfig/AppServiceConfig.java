package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.dao.*;
import com.epam.trainings.spring.core.dm.service.*;
import com.epam.trainings.spring.core.dm.service.impl.*;
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy;
import com.epam.trainings.spring.core.dm.service.impl.strategies.NthMultipleTicketDiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    @Qualifier("eventByNameAccessionsCounterDao")
    private EventCounterDao eventByNameAccessionsCounterDao;
    @Autowired
    @Qualifier("eventPriceCalculationsCounterDao")
    private EventCounterDao eventPriceCalculationsCounterDao;
    @Autowired
    @Qualifier("eventTicketsBookingsCounterDao")
    private EventCounterDao eventTicketsBookingsCounterDao;
    @Autowired
    private DiscountCounterDao discountCounterDao;
    @Autowired
    private LuckyDao luckyDao;

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
        bookingService.setEventService(eventService());
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

    @Bean
    public StatisticService eventsStatisticService() {
        StatisticServiceImpl statisticService = new StatisticServiceImpl();
        statisticService.setEventByNameAccessionsCounterDao(eventByNameAccessionsCounterDao);
        statisticService.setEventPriceCalculationsCounterDao(eventPriceCalculationsCounterDao);
        statisticService.setEventTicketsBookingsCounterDao(eventTicketsBookingsCounterDao);
        statisticService.setDiscountCounterDao(discountCounterDao);
        statisticService.setLuckyDao(luckyDao);
        return statisticService;
    }

    @Bean
    public RandomGeneratorService randomGeneratorService() {
        return new RandomGeneratorServiceImpl();
    }

    @Bean
    public DiscountService.DiscountStrategy createBirthdayDiscount() {
        BirthdayDiscountStrategy birthday = new BirthdayDiscountStrategy();
        birthday.setDiscountPercentage(birthdayDiscount);
        return birthday;
    }

    @Bean
    public DiscountService.DiscountStrategy createNthTicketDiscount() {
        NthMultipleTicketDiscountStrategy nthMultipleTicketDiscountStrategy = new NthMultipleTicketDiscountStrategy();
        nthMultipleTicketDiscountStrategy.setTicketsDao(ticketsDao);
        nthMultipleTicketDiscountStrategy.setDiscountPercentage(nthTicketDiscount);
        nthMultipleTicketDiscountStrategy.setTicketsToDiscount(ticketToDiscount);
        return nthMultipleTicketDiscountStrategy;
    }
}
