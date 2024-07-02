package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
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
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer createSendTransfer(@RequestBody int transfer_type_id, int transfer_status_id, Principal principal, int user_id_to, BigDecimal amount) {
        Transfer newTransfer = new Transfer(transfer_type_id, transfer_status_id, accountDao.getAccountByUserId(userDao.getUserByUsername(principal.getName()).getId()),accountDao.getAccountByUserId(userDao.getUserById(user_id_to).getId()), amount);
        return transferDao.createTransfer(newTransfer);
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer createRequestTransfer(@RequestBody int transfer_type_id, int transfer_status_id, int user_id_from, Principal principal, BigDecimal amount) {
        Transfer newTransfer = new Transfer(transfer_type_id, transfer_status_id, accountDao.getAccountByUserId(userDao.getUserById(user_id_from).getId()), accountDao.getAccountByUserId(userDao.getUserByUsername(principal.getName()).getId()), amount);
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
