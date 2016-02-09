package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    void delete(long id);

    User find(long id);

    List<User> findAll();

    User findByEmail(String email);

    List<User> findByName(String name);

}
