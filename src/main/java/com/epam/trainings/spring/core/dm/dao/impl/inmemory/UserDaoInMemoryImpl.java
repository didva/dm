package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.UserDao;
import com.epam.trainings.spring.core.dm.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDaoInMemoryImpl implements UserDao {

    private Map<Long, User> userMap = new HashMap<>();

    @Override
    public void add(User user) {
        user.setId(userMap.size() + 1);
        userMap.put(user.getId(), user);
    }

    @Override
    public void delete(long id) {
        userMap.remove(id);
    }

    @Override
    public User find(long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User findByEmail(String email) {
        return userMap.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public List<User> findByName(String name) {
        return userMap.values().stream().filter(u -> u.getName().equals(name)).collect(Collectors.toList());
    }
}
