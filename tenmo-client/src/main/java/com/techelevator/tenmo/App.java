package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.model.UsernameAndId;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();

    private final UserService userService = new UserService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            userService.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        consoleService.printBigDecimal(userService.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
        List<UsernameAndId> users = userService.listUsers();
        for(int i = 0; i < users.size(); i++){
            if (users.get(i).getId() == currentUser.getUser().getId()) {
                users.remove(i);
            }
        }
        consoleService.printUserList(users);
        int user_id_to =  consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        if (user_id_to != 0 && user_id_to != currentUser.getUser().getId()) {
            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount of TE Bucks to send: ");
            if (amount.compareTo(userService.getBalance()) < 1) {
                TransferRequest newTransferRequest = new TransferRequest(1, 1, currentUser.getUser().getId(), user_id_to, amount);
                Integer newTransferId = null;
                newTransferId = userService.transferRequest(newTransferRequest);
                userService.updateBalance(newTransferId);
            }
        }
	}

	private void requestBucks() {
        List<UsernameAndId> users = userService.listUsers();
        for(int i = 0; i < users.size(); i++){
            if (users.get(i).getId() == currentUser.getUser().getId()) {
                users.remove(i);
            }
        }
        consoleService.printUserList(users);
        int user_id_from =  consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
        if (user_id_from != 0) {
            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount of TE Bucks to request: ");
            TransferRequest newTransferRequest = new TransferRequest(0, 0, user_id_from, currentUser.getUser().getId(), amount);
            userService.transferRequest(newTransferRequest);
        }
	}
}
