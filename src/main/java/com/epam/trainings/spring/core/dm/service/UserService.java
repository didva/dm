package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import java.util.List;

public interface UserService {

    void register(User user);

    void remove(long id);

    User getById(long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name);

    List<Ticket> getBookedTickets(long id);

}
