package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TransferDao transferDao;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        BigDecimal balance = null;
        balance = accountDao.getBalance(userDao.getUserByUsername(principal.getName()).getId());
        return balance;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/balance", method = RequestMethod.POST)
    public void updateBalance(@RequestBody Principal principal, int id) {
        Transfer newTransfer = getTransferById(id);
        BigDecimal newFromBalance = null;
        BigDecimal newToBalance = null;
        BigDecimal nonUserBalance = null;
        BigDecimal userBalance = accountDao.getBalance(userDao.getUserByUsername(principal.getName()).getId());
        BigDecimal amount = newTransfer.getAmount();
        if (newTransfer.getTransfer_type_id() == 1) {
            nonUserBalance = accountDao.getBalance(accountDao.getUserByAccountId(newTransfer.getAccount_to()));
            newFromBalance = userBalance.subtract(amount);
            newToBalance = nonUserBalance.add(amount);
        } else if (newTransfer.getTransfer_type_id() == 0) {
            nonUserBalance = accountDao.getBalance(accountDao.getUserByAccountId(newTransfer.getAccount_from()));
            newFromBalance = nonUserBalance.subtract(amount);
            newToBalance = userBalance.add(amount);
        }
        accountDao.updateBalance(newFromBalance, newTransfer.getAccount_from());
        accountDao.updateBalance(newToBalance, newTransfer.getAccount_to());
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UsernameAndId> getUsers() {
        List<User> userList = userDao.getUsers();
        List<UsernameAndId> users = new ArrayList<>();
        for (User user: userList) {
            UsernameAndId usernameAndId = new UsernameAndId(user.getUsername(), user.getId());
            users.add(usernameAndId);
        }
        return users;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody TransferRequest transferRequest) {
        Transfer newTransfer = new Transfer(transferRequest.getTransfer_type_id(), transferRequest.getTransfer_status_id(), transferRequest.getUser_id_from(), transferRequest.getUser_id_to(), transferRequest.getAmount());
        return transferDao.createTransfer(newTransfer);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public Transfer updateTransfer(@RequestBody Transfer updatedTransfer) {
        return transferDao.updateTransfer(updatedTransfer);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> getTransfers() {
        return transferDao.getTransfers();
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransferById(id);
    }
}
