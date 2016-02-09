package com.epam.trainings.spring.core.dm;

import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        // For java-based configuration - uncomment line below and import classes.
        // ApplicationContext context = new AnnotationConfigApplicationContext(AppDaoConfig.class, AppServiceConfig.class, AppUiConfig.class);
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Menu menu = context.getBean(Menu.class);
        ConsoleReader reader = context.getBean(ConsoleReader.class);
        while (menu != null) {
            menu.printMenu();
            menu = menu.select(reader.readInt());
        }
    }

}
