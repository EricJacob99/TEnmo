package com.techelevator.tenmo.model;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private int account_from;
    private int account_to;
    private BigDecimal amount;

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TransferDao transferDao;

    public int getTransfer_id() {
        return transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public int getAccount_from() {
        return account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transfer_id=" + transfer_id +
                ", transfer_type_id=" + transfer_type_id +
                ", transfer_status_id=" + transfer_status_id +
                ", account_from=" + account_from +
                ", account_to=" + account_to +
                ", amount=" + amount +
                '}';
    }

    public Transfer() {
    }

    public Transfer(int transfer_type_id, int transfer_status_id, int user_id_from, int user_id_to, BigDecimal amount) {
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = accountDao.getAccountByUserId(userDao.getUserById(user_id_to).getId());
        this.account_to = accountDao.getAccountByUserId(userDao.getUserById(user_id_to).getId());
        this.amount = amount;
    }

    public Transfer(int transfer_id, int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal amount) {
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }

}
