package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import com.epam.trainings.spring.core.dm.dao.AssignedEventsDao;
import com.epam.trainings.spring.core.dm.model.AssignedEvent;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class AssignedEventsJdbcDao extends BaseJdbcDao<AssignedEvent> implements AssignedEventsDao {

    private static final String INSERT_SQL = "INSERT INTO assigned_events (event_id, auditorium, date_time) VALUES (?, ?, ?)";
    private static final String FIND_SQL = "SELECT * FROM assigned_events WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM assigned_events";
    private static final String FIND_BY_EVENT_SQL = "SELECT * FROM assigned_events WHERE event_id = ?";
    private static final String FIND_BY_EVENT_AND_DATE_SQL = "SELECT * FROM assigned_events WHERE event_id = ? AND date_time = ?";
    private static final String FIND_BY_AUDITORIUM_SQL = "SELECT * FROM assigned_events WHERE auditorium = ? AND date_time = ?";
    private static final String FIND_BY_RANGE_SQL = "SELECT * FROM assigned_events WHERE date_time > ? AND date_time <= ?";

    @Override
    public AssignedEvent find(long id) {
        return queryForObject(FIND_SQL, new Object[]{id}, new AssignedEventRowMapper());
    }

    @Override
    public List<AssignedEvent> findAll() {
        return getJdbcTemplate().query(FIND_ALL_SQL, new AssignedEventRowMapper());
    }

    @Override
    public List<AssignedEvent> findByRange(LocalDateTime from, LocalDateTime to) {
        return getJdbcTemplate().query(FIND_BY_RANGE_SQL, new Object[]{Timestamp.valueOf(from), Timestamp.valueOf(to)}, new AssignedEventRowMapper());
    }

    @Override
    public void assignAuditorium(AssignedEvent assignedEvent) {
        long id = insert(INSERT_SQL, assignedEvent.getEventId(), assignedEvent.getAuditorium(), Timestamp.valueOf(assignedEvent.getDateTime()));
        assignedEvent.setId(id);
    }

    @Override
    public List<AssignedEvent> findByEvent(long eventId) {
        return getJdbcTemplate().query(FIND_BY_EVENT_SQL, new Object[]{eventId}, new AssignedEventRowMapper());
    }

    @Override
    public AssignedEvent findByEvent(long eventId, LocalDateTime dateTime) {
        return queryForObject(FIND_BY_EVENT_AND_DATE_SQL, new Object[]{eventId, Timestamp.valueOf(dateTime)}, new AssignedEventRowMapper());
    }

    @Override
    public AssignedEvent findByAuditorium(String auditorium, LocalDateTime dateTime) {
        return queryForObject(FIND_BY_AUDITORIUM_SQL, new Object[]{auditorium, Timestamp.valueOf(dateTime)}, new AssignedEventRowMapper());
    }


    private class AssignedEventRowMapper implements RowMapper<AssignedEvent> {

        @Override
        public AssignedEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssignedEvent assignedEvent = new AssignedEvent();
            int index = 1;
            assignedEvent.setId(rs.getLong(index++));
            assignedEvent.setEventId(rs.getLong(index++));
            assignedEvent.setAuditorium(rs.getString(index++));
            assignedEvent.setDateTime(rs.getTimestamp(index).toLocalDateTime());
            return assignedEvent;
        }
    }
}
