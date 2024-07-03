package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferRequest {
    private int transfer_type_id;
    private int transfer_status_id;
    private int user_id_from;
    private int user_id_to;
    private BigDecimal amount;

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getUser_id_from() {
        return user_id_from;
    }

    public void setUser_id_from(int user_id_from) {
        this.user_id_from = user_id_from;
    }

    public int getUser_id_to() {
        return user_id_to;
    }

    public void setUser_id_to(int user_id_to) {
        this.user_id_to = user_id_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferRequest(){}

    public TransferRequest(int transfer_type_id, int transfer_status_id, int user_id_from, int user_id_to, BigDecimal amount) {
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.user_id_from = user_id_from;
        this.user_id_to = user_id_to;
        this.amount = amount;
    }
}
