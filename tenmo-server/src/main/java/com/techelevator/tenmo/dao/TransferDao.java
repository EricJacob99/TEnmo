package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();
    Transfer getTransferById(int id);
    Transfer updateTransfer(int id);
    Transfer createTransfer();
}
