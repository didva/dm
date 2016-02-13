package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.service.*;
import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;
import com.epam.trainings.spring.core.dm.ui.menus.console.AdminMenu;
import com.epam.trainings.spring.core.dm.ui.menus.console.BaseMenu;
import com.epam.trainings.spring.core.dm.ui.menus.console.StatisticsMenu;
import com.epam.trainings.spring.core.dm.ui.menus.console.UserMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppUiConfig {

    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatisticService statisticService;

    @Bean
    public ConsoleReader consoleReader() {
        return new ConsoleReader();
    }

    @Bean
    public Menu baseMenu() {
        BaseMenu baseMenu = new BaseMenu();

        Menu adminMenu = getAdminMenu(baseMenu);
        Menu userMenu = getUserMenu(baseMenu);
        Menu statisticMenu = getStatisticMenu(baseMenu);

        baseMenu.setAdminMenu(adminMenu);
        baseMenu.setUserMenu(userMenu);
        baseMenu.setStatisticsMenu(statisticMenu);
        return baseMenu;
    }

    private Menu getAdminMenu(Menu parent) {
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.setParent(parent);
        adminMenu.setConsoleReader(consoleReader());
        adminMenu.setAuditoriumService(auditoriumService);
        adminMenu.setBookingService(bookingService);
        adminMenu.setEventService(eventService);
        return adminMenu;
    }

    private Menu getUserMenu(Menu parent) {
        UserMenu userMenu = new UserMenu();
        userMenu.setParent(parent);
        userMenu.setConsoleReader(consoleReader());
        userMenu.setUserService(userService);
        userMenu.setAuditoriumService(auditoriumService);
        userMenu.setBookingService(bookingService);
        userMenu.setEventService(eventService);
        return userMenu;
    }

    private Menu getStatisticMenu(Menu parent) {
        StatisticsMenu statisticsMenu = new StatisticsMenu();
        statisticsMenu.setParent(parent);
        statisticsMenu.setConsoleReader(consoleReader());
        statisticsMenu.setStatisticService(statisticService);
        return statisticsMenu;
    }
}
