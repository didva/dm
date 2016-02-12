package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.dao.*;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@Configuration
@PropertySource("classpath:config.properties")
public class AppDaoConfig {

    @Value("${auditorium.properties.path}")
    private String auditoriumsPath;

    @Bean
    public UserDao userDao() {
        return new UserDaoInMemoryImpl();
    }

    @Bean
    public AuditoriumDao auditoriumDao() throws IOException {
        return new AuditoriumDaoInMemoryImpl(auditoriumsPath);
    }

    @Bean
    public TicketsDao ticketsDao() {
        return new TicketsDaoInMemoryImpl();
    }

    @Bean
    public EventDao eventDao() {
        return new EventDaoInMemoryImpl();
    }

    @Bean
    public AssignedEventsDao assignedEventsDao() {
        return new AssignedEventsDaoInMemoryImpl();
    }

    @Bean
    public EventCounterDao eventByNameAccessionsCounterDao() {
        return new GeneralEventCounterDaoInMemoryImpl();
    }

    @Bean
    public EventCounterDao eventPriceCalculationsCounterDao() {
        return new GeneralEventCounterDaoInMemoryImpl();
    }

    @Bean
    public EventCounterDao eventTicketsBookingsCounterDao() {
        return new GeneralEventCounterDaoInMemoryImpl();
    }

}
