package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import com.epam.trainings.spring.core.dm.dao.EventCounterDao;
import com.epam.trainings.spring.core.dm.model.Counter;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneralEventCounterJdbcDao extends BaseJdbcDao<Counter<Long>> implements EventCounterDao {

    private static final String INSERT_SQL = "INSERT INTO %s (event_id, times) VALUES (?, 1)";
    private static final String UPDATE_SQL = "UPDATE %s SET times = (times + 1) WHERE event_id = ?";
    private static final String FIND_BY_EVENT_SQL = "SELECT event_id, times FROM %s WHERE event_id = ?";

    private String tableName;

    @Override
    public void increase(long eventId) {
        Counter<Long> counter = queryForObject(String.format(FIND_BY_EVENT_SQL, tableName), new Object[]{eventId}, new EventCounterRowMapper());
        if (counter == null) {
            insert(String.format(INSERT_SQL, tableName), eventId);
        } else {
            getJdbcTemplate().update(String.format(UPDATE_SQL, tableName), eventId);
        }
    }

    @Override
    public Counter<Long> findByEventId(long eventId) {
        Counter<Long> counter = queryForObject(String.format(FIND_BY_EVENT_SQL, tableName), new Object[]{eventId}, new EventCounterRowMapper());
        if (counter == null) {
            return new Counter<>(eventId, 0);
        }
        return counter;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private class EventCounterRowMapper implements RowMapper<Counter<Long>> {

        @Override
        public Counter<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Counter<Long> counter = new Counter<>();
            int index = 1;
            counter.setItemId(rs.getLong(index++));
            counter.setTimes(rs.getInt(index));
            return counter;
        }
    }
}
