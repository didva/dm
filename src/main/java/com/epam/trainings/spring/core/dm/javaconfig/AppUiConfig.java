package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.service.AuditoriumService;
import com.epam.trainings.spring.core.dm.service.BookingService;
import com.epam.trainings.spring.core.dm.service.EventService;
import com.epam.trainings.spring.core.dm.service.UserService;
import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;
import com.epam.trainings.spring.core.dm.ui.menus.console.AdminMenu;
import com.epam.trainings.spring.core.dm.ui.menus.console.BaseMenu;
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

    @Bean
    public ConsoleReader consoleReader() {
        return new ConsoleReader();
    }

    @Bean
    public Menu userMenu() {
        BaseMenu baseMenu = new BaseMenu();

        Menu adminMenu = getAdminMenu(baseMenu);
        Menu userMenu = getUserMenu(baseMenu);

        baseMenu.setAdminMenu(adminMenu);
        baseMenu.setUserMenu(userMenu);
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
}
