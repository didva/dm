package com.epam.trainings.spring.core.dm.ui.menus.console;

import com.epam.trainings.spring.core.dm.service.StatisticService;
import com.epam.trainings.spring.core.dm.ui.ConsoleReader;
import com.epam.trainings.spring.core.dm.ui.Menu;

public class StatisticsMenu implements Menu {

    private Menu parent;
    private ConsoleReader consoleReader;
    private StatisticService statisticService;

    @Override
    public void printMenu() {
        System.out.println("1 - How many times event was accessed by name");
        System.out.println("2 - How many times event's prices were queried");
        System.out.println("3 - How many times event's tickets were booked");
        System.out.println("4 - How many times each discount was given total");
        System.out.println("5 - How many times each discount was given for user");
        System.out.println("6 - How many times user was lucky");
        System.out.println("7 - How many times users were lucky for event");
        System.out.println("0 - Back");
    }

    @Override
    public Menu select(int num) {
        switch (num) {
            case 1:
                printEventByNameStatistic();
                break;
            case 2:
                printEventsPriceStatistic();
                break;
            case 3:
                printEventsTicketsStatistic();
                break;
            case 4:
                printAllDiscounts();
                break;
            case 5:
                printDiscountsForUser();
                break;
            case 6:
                printLuckyInfoByUser();
                break;
            case 7:
                printLuckyInfoByEvent();
                break;
            case 0:
                return parent;
            default:
                System.out.println("Incorrect choice!");
        }
        return this;
    }

    private void printLuckyInfoByEvent() {
        long eventId = getEventId();
        System.out.println(statisticService.getLuckyInfoByEventId(eventId));
    }

    private void printLuckyInfoByUser() {
        long userId = getUserId();
        System.out.println(statisticService.getLuckyInfoByUserId(userId));
    }

    private void printDiscountsForUser() {
        long userId = getUserId();
        System.out.println(statisticService.getDiscountsByUser(userId));
    }

    private void printAllDiscounts() {
        System.out.println(statisticService.getAllDiscounts());
    }

    private void printEventsTicketsStatistic() {
        long eventId = getEventId();
        System.out.println(statisticService.getBookedTimes(eventId));
    }

    private void printEventsPriceStatistic() {
        long eventId = getEventId();
        System.out.println(statisticService.getRequestsForPrices(eventId));
    }

    private void printEventByNameStatistic() {
        long eventId = getEventId();
        System.out.println(statisticService.getRequestsByName(eventId));
    }

    private long getEventId() {
        System.out.println("Please enter event id: ");
        return consoleReader.readLong();
    }

    private long getUserId() {
        System.out.println("Please enter user id: ");
        return consoleReader.readLong();
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public void setConsoleReader(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
