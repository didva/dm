package com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

public abstract class BaseJdbcDao<T> extends JdbcDaoSupport {

    protected long insert(String sql, Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (args != null) {
                for (int i = 0; i < args.length; ++i) {
                    ps.setObject(i + 1, args[i]);
                }
            }
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    protected T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        try {
            return getJdbcTemplate().queryForObject(sql, args, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
