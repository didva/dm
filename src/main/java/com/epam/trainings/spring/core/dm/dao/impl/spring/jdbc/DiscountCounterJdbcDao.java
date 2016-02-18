package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import com.epam.trainings.spring.core.dm.dao.DiscountCounterDao;
import com.epam.trainings.spring.core.dm.model.Counter;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DiscountCounterJdbcDao extends BaseJdbcDao<Counter<String>> implements DiscountCounterDao {

    private static final String INSERT_SQL = "INSERT INTO discounts (name, user_id, times) VALUES (?, ?, 1)";
    private static final String UPDATE_SQL = "UPDATE discounts SET times = (times + 1) WHERE name = ? AND user_id = ?";
    private static final String FIND_ALL_SQL = "SELECT name, times FROM discounts";
    private static final String FIND_BY_USER_SQL = "SELECT name, times FROM discounts WHERE user_id = ?";
    private static final String FIND_BY_NAME_AND_USER_SQL = "SELECT name, times FROM discounts WHERE name = ? AND user_id = ?";

    @Override
    public void increase(String discountName, long userId) {
        Counter<String> counter = queryForObject(FIND_BY_NAME_AND_USER_SQL, new Object[]{discountName, userId}, new DiscountCounterRowMapper());
        if (counter == null) {
            insert(INSERT_SQL, discountName, userId);
        } else {
            getJdbcTemplate().update(UPDATE_SQL, discountName, userId);
        }
    }

    @Override
    public List<Counter<String>> findAll() {
        return getJdbcTemplate().query(FIND_ALL_SQL, new DiscountCounterRowMapper());
    }

    @Override
    public List<Counter<String>> findByUserId(long userId) {
        return getJdbcTemplate().query(FIND_BY_USER_SQL, new Object[]{userId}, new DiscountCounterRowMapper());
    }

    private class DiscountCounterRowMapper implements RowMapper<Counter<String>> {

        @Override
        public Counter<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Counter<String> counter = new Counter<>();
            int index = 1;
            counter.setItemId(rs.getString(index++));
            counter.setTimes(rs.getInt(index));
            return counter;
        }
    }
}
