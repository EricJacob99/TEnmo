package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceByUserId(int id);
    BigDecimal getBalanceByAccountId(int id);

    void updateBalance(BigDecimal balance, int account_id);

    Integer getAccountByUserId(int userId);

    Integer getUserByAccountId(int accountId);
}
