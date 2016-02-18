package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import com.epam.trainings.spring.core.dm.dao.EventDao;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.Rating;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EventJdbcDao extends BaseJdbcDao<Event> implements EventDao {

    private static final String INSERT_SQL = "INSERT INTO events (name, price, rating) VALUES (?, ?, ?)";
    private static final String DELETE_SQL = "DELETE FROM events WHERE id = ?";
    private static final String FIND_SQL = "SELECT * FROM events WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM events";
    private static final String FIND_BY_NAME_SQL = "SELECT * FROM events WHERE name = ?";

    @Override
    public void add(Event event) {
        long id = insert(INSERT_SQL, event.getName(), event.getPrice(), event.getRating().toString());
        event.setId(id);
    }

    @Override
    public void delete(long id) {
        getJdbcTemplate().update(DELETE_SQL, id);
    }

    @Override
    public Event find(long id) {
        return queryForObject(FIND_SQL, new Object[]{id}, new EventRowMapper());
    }

    @Override
    public Event findByName(String name) {
        return queryForObject(FIND_BY_NAME_SQL, new Object[]{name}, new EventRowMapper());
    }

    @Override
    public List<Event> findAll() {
        return getJdbcTemplate().query(FIND_ALL_SQL, new EventRowMapper());
    }

    private class EventRowMapper implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            int index = 1;
            event.setId(rs.getLong(index++));
            event.setName(rs.getString(index++));
            event.setPrice(rs.getDouble(index++));
            event.setRating(Rating.valueOf(rs.getString(index)));
            return event;
        }

    }
}
