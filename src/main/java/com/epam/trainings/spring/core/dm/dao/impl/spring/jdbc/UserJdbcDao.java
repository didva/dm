package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import com.epam.trainings.spring.core.dm.dao.UserDao;
import com.epam.trainings.spring.core.dm.model.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserJdbcDao extends BaseJdbcDao<User> implements UserDao {

    private static final String INSERT_SQL = "INSERT INTO users (name, email, birth_date) VALUES (?, ?, ?)";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private static final String FIND_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM users";
    private static final String FIND_BY_NAME_SQL = "SELECT * FROM users WHERE name = ?";
    private static final String FIND_BY_EMAIL_SQL = "SELECT * FROM users WHERE email = ?";

    @Override
    public void add(User user) {
        long id = insert(INSERT_SQL, user.getName(), user.getEmail(), Date.valueOf(user.getBirthDate()));
        user.setId(id);
    }

    @Override
    public void delete(long id) {
        getJdbcTemplate().update(DELETE_SQL, id);
    }

    @Override
    public User find(long id) {
        return queryForObject(FIND_SQL, new Object[]{id}, new UserRowMapper());
    }

    @Override
    public List<User> findAll() {
        return getJdbcTemplate().query(FIND_ALL_SQL, new UserRowMapper());
    }

    @Override
    public User findByEmail(String email) {
        return queryForObject(FIND_BY_EMAIL_SQL, new Object[]{email}, new UserRowMapper());
    }

    @Override
    public List<User> findByName(String name) {
        return getJdbcTemplate().query(FIND_BY_NAME_SQL, new Object[]{name}, new UserRowMapper());
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            int index = 1;
            user.setId(rs.getLong(index++));
            user.setName(rs.getString(index++));
            user.setEmail(rs.getString(index++));
            user.setBirthDate(rs.getDate(index).toLocalDate());
            return user;
        }

    }
}
