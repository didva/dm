package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.AuditoriumDao;
import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.dao.UserDao;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.DiscountService;
import com.epam.trainings.spring.core.dm.service.EventService;
import com.epam.trainings.spring.core.dm.service.RandomGeneratorService;
import com.epam.trainings.spring.core.dm.service.StatisticService;
import com.epam.trainings.spring.core.dm.service.UserService;
import com.epam.trainings.spring.core.dm.service.impl.AuditoriumServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.BookingServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.DiscountServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.EventServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.RandomGeneratorServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.StatisticServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.UserServiceImpl;
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy;
import com.epam.trainings.spring.core.dm.service.impl.strategies.NthMultipleTicketDiscountStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:config.properties")
@EnableTransactionManagement
public class AppServiceConfig {

    @Value("${discount.birthday.discountPercentage}")
    private double birthdayDiscount;
    @Value("${discount.nthTicket.discountPercentage}")
    private double nthTicketDiscount;
    @Value("${discount.nthTicket.ticketsToDiscount}")
    private int ticketToDiscount;

    @Autowired
    private DataSource dataSource;
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
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

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
        statisticService.setEventService(eventService());
        statisticService.setEventByNameAccessionsCounterDao(eventByNameAccessionsCounterDao);
        statisticService.setEventPriceCalculationsCounterDao(eventPriceCalculationsCounterDao);
        statisticService.setEventTicketsBookingsCounterDao(eventTicketsBookingsCounterDao);
        statisticService.setDiscountCounterDao(discountCounterDao);
        statisticService.setLuckyDao(luckyDao);
        statisticService.setTicketsDao(ticketsDao);
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
