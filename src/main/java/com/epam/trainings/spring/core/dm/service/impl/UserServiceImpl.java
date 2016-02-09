package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.dao.UserDao;
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private TicketsDao ticketsDao;

    @Override
    public void register(User user) {
        if (user == null || user.getName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException();
        }
        User existingUser = this.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new AlreadyExistsException();
        }
        userDao.add(user);
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public void remove(long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        userDao.delete(id);
    }

    @Override
    public User getById(long id) {
        return userDao.find(id);
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        return userDao.findByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        return userDao.findByName(name);
    }

    @Override
    public List<Ticket> getBookedTickets(long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        return ticketsDao.findByUserId(id);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTicketsDao(TicketsDao ticketsDao) {
        this.ticketsDao = ticketsDao;
    }
}
