package com.epam.trainings.spring.core.dm.ui.menus.console;

import com.epam.trainings.spring.core.dm.ui.Menu;

public class BaseMenu implements Menu {

    private Menu adminMenu;
    private Menu userMenu;

    @Override
    public void printMenu() {
        System.out.println("1 - Admin menu");
        System.out.println("2 - User menu");
        System.out.println("0 - Exit");
    }

    @Override
    public Menu select(int num) {
        switch (num) {
            case 1:
                return adminMenu;
            case 2:
                return userMenu;
            case 0:
                return null;
            default:
                System.out.println("Incorrect choice!");
        }
        return this;
    }

    public void setAdminMenu(Menu adminMenu) {
        this.adminMenu = adminMenu;
    }

    public void setUserMenu(Menu userMenu) {
        this.userMenu = userMenu;
    }
}
