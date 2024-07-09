package com.techelevator.tenmo.services;


import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.UsernameAndId;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private String authToken = null;
    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getBalance() {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    private HttpEntity<Integer> makeIdTransferEntity(Integer transfer_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer_id, headers);
    }

    public void updateBalance(int id) {
        try {
            restTemplate.exchange(baseUrl + "balance", HttpMethod.PUT, makeIdTransferEntity(id), Void.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
            System.out.println(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferRequest> makeTransferEntity(TransferRequest transferRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transferRequest, headers);
    }

    public Integer transferRequest(TransferRequest transferRequest) {
        Integer transfer_id = null;
        try {
            HttpEntity<TransferRequest> test = makeTransferEntity(transferRequest);
            ResponseEntity<Integer> response = restTemplate.exchange(baseUrl + "transfer", HttpMethod.POST, test, Integer.class);
            transfer_id = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer_id;
    }

    public List<UsernameAndId> listUsers() {
        List<UsernameAndId> usernameAndIdList = new ArrayList<>();
        UsernameAndId[] tempArray = null;
        try {
            ResponseEntity<UsernameAndId[]> response  = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), UsernameAndId[].class);
            tempArray = response.getBody();
            usernameAndIdList.addAll(List.of(tempArray));
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return usernameAndIdList;
    }

//    public List<Transfer> listTransfers() {
//        List<Transfer> transferHistory = new ArrayList<>();
//        try {
//            ResponseEntity<Transfer> response  = restTemplate.exchange(baseUrl + "transfer", HttpMethod.GET, makeAuthEntity(), Transfer.class);
//            transferHistory.add(response.getBody());
//        }catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return transferHistory;
//    }

//    public List<Transfer> listPendingTransfers() {
//        List<Transfer> pendingTransfers = new ArrayList<>();
//        try {
//            ResponseEntity<Transfer> response  = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), Transfer.class);
//            pendingTransfers.add(response.getBody());
//        }catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return pendingTransfers;
//    }
}