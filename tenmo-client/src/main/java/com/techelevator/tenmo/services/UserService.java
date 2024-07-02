package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

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

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public void transferRequest(TransferRequest transferRequest) {
        // type, status, user_id_from, user_id_to, amount
        try {
            restTemplate.postForObject(baseUrl + "transfer", transferRequest, TransferRequest.class);
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

}