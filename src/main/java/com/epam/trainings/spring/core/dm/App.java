package com.epam.trainings.spring.core.dm;

import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Menu menu = context.getBean(Menu.class);
        ConsoleReader reader = context.getBean(ConsoleReader.class);
        while (menu != null) {
            menu.printMenu();
            menu = menu.select(reader.readInt());
        }
    }

}
