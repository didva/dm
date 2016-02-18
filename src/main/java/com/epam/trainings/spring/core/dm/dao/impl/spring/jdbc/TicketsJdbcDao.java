package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;


import com.epam.trainings.spring.core.dm.dao.TicketsDao;
import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketsJdbcDao extends BaseJdbcDao<Ticket> implements TicketsDao {

    private static final String INSERT_TICKET_SQL = "INSERT INTO tickets (assigned_event_id, user_id, final_price) VALUES (?, ?, ?)";
    private static final String INSERT_SEATS_SQL = "INSERT INTO seats (ticket_id, seat) VALUES (?, ?)";
    private static final String FIND_TICKET_SQL = "SELECT * FROM tickets WHERE id = ?";
    private static final String FIND_TICKET_BY_USER_SQL = "SELECT * FROM tickets WHERE user_id = ?";
    private static final String FIND_TICKET_BY_ASSIGNED_EVENT_SQL = "SELECT * FROM tickets WHERE assigned_event_id = ?";
    private static final String FIND_SEATS_SQL = "SELECT * FROM seats WHERE ticket_id = ?";

    @Override
    public Ticket find(long id) {
        Ticket ticket = queryForObject(FIND_TICKET_SQL, new Object[]{id}, new TicketRowMapper());
        if (ticket == null) {
            return null;
        }
        ticket.setSeats(getSeats(ticket.getId()));
        return ticket;
    }

    @Override
    public List<Ticket> findByUserId(long userId) {
        List<Ticket> tickets = getJdbcTemplate().query(FIND_TICKET_BY_USER_SQL, new Object[]{userId}, new TicketRowMapper());
        tickets.stream().forEach(t -> t.setSeats(getSeats(t.getId())));
        return tickets;
    }

    @Override
    public void add(User user, Ticket ticket) {
        long id = insert(INSERT_TICKET_SQL, ticket.getAssignedEventId(), user == null ? null : user.getId(), ticket.getFinalPrice());
        ticket.setId(id);
        ticket.getSeats().stream().forEach(seat -> insert(INSERT_SEATS_SQL, new Object[]{id, seat}));
    }

    @Override
    public List<Ticket> findByEvent(long assignedEventId) {
        List<Ticket> tickets = getJdbcTemplate().query(FIND_TICKET_BY_ASSIGNED_EVENT_SQL, new Object[]{assignedEventId}, new TicketRowMapper());
        tickets.stream().forEach(t -> t.setSeats(getSeats(t.getId())));
        return tickets;
    }

    private Set<Integer> getSeats(long ticketId) {
        return new HashSet<>(getJdbcTemplate().query(FIND_SEATS_SQL, new Object[]{ticketId}, new SeatRowMapper()));
    }

    private class TicketRowMapper implements RowMapper<Ticket> {

        @Override
        public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ticket ticket = new Ticket();
            int index = 1;
            ticket.setId(rs.getLong(index++));
            ticket.setAssignedEventId(rs.getLong(index++));
            ticket.setUserId(rs.getLong(index++));
            if (rs.wasNull()) {
                ticket.setUserId(null);
            }
            ticket.setFinalPrice(rs.getDouble(index));
            return ticket;
        }
    }

    private class SeatRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("seat");
        }
    }
}
