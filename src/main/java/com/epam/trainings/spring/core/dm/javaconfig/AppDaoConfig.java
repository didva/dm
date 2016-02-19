package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.dao.AuditoriumDao;
import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.dao.UserDao;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.AuditoriumDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.LuckyDaoInMemoryImpl;
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.AssignedEventsJdbcDao;
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.DiscountCounterJdbcDao;
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.EventJdbcDao;
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.GeneralEventCounterJdbcDao;
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.TicketsJdbcDao;
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.UserJdbcDao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.IOException;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:config.properties", "classpath:db/table-names.properties"})
public class AppDaoConfig {

    @Value("${auditorium.properties.path}")
    private String auditoriumsPath;
    @Value("${statistic.events.by.name.table.name}")
    private String eventsByNameTableName;
    @Value("${statistic.events.price.table.name}")
    private String eventsPriceTableName;
    @Value("${statistic.events.tickets.table.name}")
    private String eventsTicketsTableName;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("db/create-db.sql").build();
    }

    @Bean
    public UserDao userDao() {
        UserJdbcDao userJdbcDao = new UserJdbcDao();
        userJdbcDao.setDataSource(dataSource());
        return userJdbcDao;
    }

    @Bean
    public AuditoriumDao auditoriumDao() throws IOException {
        return new AuditoriumDaoInMemoryImpl(auditoriumsPath);
    }

    @Bean
    public TicketsDao ticketsDao() {
        TicketsJdbcDao ticketsJdbcDao = new TicketsJdbcDao();
        ticketsJdbcDao.setDataSource(dataSource());
        return ticketsJdbcDao;
    }

    @Bean
    public EventDao eventDao() {
        EventJdbcDao eventJdbcDao = new EventJdbcDao();
        eventJdbcDao.setDataSource(dataSource());
        return eventJdbcDao;
    }

    @Bean
    public AssignedEventsDao assignedEventsDao() {
        AssignedEventsJdbcDao assignedEventsJdbcDao = new AssignedEventsJdbcDao();
        assignedEventsJdbcDao.setDataSource(dataSource());
        return assignedEventsJdbcDao;
    }

    @Bean
    public EventCounterDao eventByNameAccessionsCounterDao() {
        GeneralEventCounterJdbcDao generalEventCounterJdbcDao = new GeneralEventCounterJdbcDao();
        generalEventCounterJdbcDao.setDataSource(dataSource());
        generalEventCounterJdbcDao.setTableName(eventsByNameTableName);
        return generalEventCounterJdbcDao;
    }

    @Bean
    public EventCounterDao eventPriceCalculationsCounterDao() {
        GeneralEventCounterJdbcDao generalEventCounterJdbcDao = new GeneralEventCounterJdbcDao();
        generalEventCounterJdbcDao.setDataSource(dataSource());
        generalEventCounterJdbcDao.setTableName(eventsPriceTableName);
        return generalEventCounterJdbcDao;
    }

    @Bean
    public EventCounterDao eventTicketsBookingsCounterDao() {
        GeneralEventCounterJdbcDao generalEventCounterJdbcDao = new GeneralEventCounterJdbcDao();
        generalEventCounterJdbcDao.setDataSource(dataSource());
        generalEventCounterJdbcDao.setTableName(eventsTicketsTableName);
        return generalEventCounterJdbcDao;
    }

    @Bean
    public DiscountCounterDao discountCounterDao() {
        DiscountCounterJdbcDao discountCounterJdbcDao = new DiscountCounterJdbcDao();
        discountCounterJdbcDao.setDataSource(dataSource());
        return discountCounterJdbcDao;
    }

    @Bean
    public LuckyDao luckyDao() {
        return new LuckyDaoInMemoryImpl();
    }

}
