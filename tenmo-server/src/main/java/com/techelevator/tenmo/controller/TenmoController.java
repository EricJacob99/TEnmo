package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        BigDecimal balance = null;
        balance = accountDao.getBalanceByUserId(userDao.getUserByUsername(principal.getName()).getId());
        return balance;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.PUT)
    public void updateBalance(@RequestBody int id) {
        Transfer transfer = transferDao.getTransferById(id);
        int accountFrom = transfer.getAccount_from();
        int accountTo = transfer.getAccount_to();
        BigDecimal accountFromBalance = accountDao.getBalanceByAccountId(accountFrom);
        BigDecimal accountToBalance = accountDao.getBalanceByAccountId(accountTo);
        // we were getting balance by user Id but we were passing Account Id
        BigDecimal amount = getTransferById(id).getAmount();

        BigDecimal newFromBalance = accountFromBalance.subtract(amount);
        BigDecimal newToBalance = accountToBalance.add(amount);
        try {
            accountDao.updateBalance(newFromBalance, accountFrom);
            accountDao.updateBalance(newToBalance, accountTo);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public int createTransfer(@RequestBody TransferRequest transferRequest) {
        Transfer newTransfer = new Transfer(transferRequest.getTransfer_type_id(),
                transferRequest.getTransfer_status_id(),
                accountDao.getAccountByUserId(transferRequest.getUser_id_from()),
                accountDao.getAccountByUserId(transferRequest.getUser_id_to()),
                transferRequest.getAmount());

                newTransfer= transferDao.createTransfer(newTransfer);
                //added the left side, transferDao was creating a new transfer but it was not setting it to new transfer variable we declared
                return newTransfer.getTransfer_id();
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
