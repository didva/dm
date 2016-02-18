package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.AuditoriumDao;
import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.dao.UserDao;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.AssignedEventsDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.AuditoriumDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.DiscountCounterDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.EventDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.GeneralEventCounterDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.LuckyDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.TicketsDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.UserDaoInMemoryImpl;

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

    @Bean
    public DiscountCounterDao discountCounterDao() {
        return new DiscountCounterDaoInMemoryImpl();
    }

    @Bean
    public LuckyDao luckyDao() {
        return new LuckyDaoInMemoryImpl();
    }

}
