package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalance(int id) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                balance = mapRowToBalance(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balance;

    }

    @Override
    public void updateBalance(BigDecimal balance, int account_id) {
            String sql = "UPDATE account SET balance  = ? WHERE account_id = ?;";
            try {
                jdbcTemplate.update(sql,balance, account_id);
            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Unable to connect to server or database", e);
            } catch (DataIntegrityViolationException e) {
                throw new DaoException("Data integrity violation", e);
            } catch (NullPointerException e) {
                throw new DaoException("Null pointer exception", e);
            }
        }

    @Override
    public Integer getAccountByUserId(int userId) {
        Integer accountId = null;
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                accountId = mapRowToAccountId(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accountId;
    }

    @Override
    public Integer getUserByAccountId(int accountId) {
        Integer userId = null;
        String sql = "SELECT user_id FROM account WHERE account_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                userId = mapRowToUserId(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userId;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
    private BigDecimal mapRowToBalance(SqlRowSet rs) {
        BigDecimal balance = null;
        balance = rs.getBigDecimal("balance");
        return balance;
    }

    private Integer mapRowToAccountId(SqlRowSet rs) {
        Integer accountId = null;
        accountId = rs.getInt("account_id");
        return accountId;
    }

    private Integer mapRowToUserId(SqlRowSet rs) {
        Integer userId = null;
        userId = rs.getInt("user_id");
        return userId;
    }
}
