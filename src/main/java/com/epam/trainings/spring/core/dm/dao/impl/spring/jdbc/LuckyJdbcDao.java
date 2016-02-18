package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import com.epam.trainings.spring.core.dm.dao.LuckyDao;
import com.epam.trainings.spring.core.dm.model.LuckyInfo;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LuckyJdbcDao extends BaseJdbcDao<LuckyInfo> implements LuckyDao {

    private static final String INSERT_SQL = "INSERT INTO lucky_info (user_id, ticket_id) VALUES (?, ?)";
    private static final String FIND_BY_USER_SQL = "SELECT * FROM lucky_info WHERE user_id = ?";
    private static final String FIND_BY_TICKET_SQL = "SELECT * FROM lucky_info WHERE ticket_id = ?";

    @Override
    public void register(long ticketId, long userId) {
        insert(INSERT_SQL, userId, ticketId);
    }

    @Override
    public LuckyInfo findByTicketId(long ticketId) {
        return queryForObject(FIND_BY_TICKET_SQL, new Object[]{ticketId}, new LuckyInfoRowMapper());
    }

    @Override
    public List<LuckyInfo> findByUserId(long userId) {
        return getJdbcTemplate().query(FIND_BY_USER_SQL, new Object[]{userId}, new LuckyInfoRowMapper());
    }

    private class LuckyInfoRowMapper implements RowMapper<LuckyInfo> {

        @Override
        public LuckyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            LuckyInfo luckyInfo = new LuckyInfo();
            int index = 1;
            luckyInfo.setId(rs.getLong(index++));
            luckyInfo.setUserId(rs.getLong(index++));
            luckyInfo.setTicketId(rs.getLong(index));
            return luckyInfo;
        }
    }
}
