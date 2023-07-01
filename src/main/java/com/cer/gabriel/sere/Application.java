package com.cer.gabriel.sere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import jakarta.annotation.PostConstruct;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Controller
@SpringBootApplication
public class Application {

	@Autowired
	private AccountRestController accountController;
	private static final Log log = LogFactory.getLog(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public Application(AccountRestController accountController) {
		this.accountController = accountController;
	}

	public void performOperations() {
		try {
			String balance = accountController.getBalance();
			String transactions = accountController.getTransactions();
			String transfer = accountController.getMoneyTransferAccountingProof("14537780");

			System.out.println("Balance: " + balance);
			System.out.println("Transactions: " + transactions);
			System.out.println("Transfer: " + transfer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/")
	public String showAccountInformation(Model model) {
		try {
			String balance = accountController.getBalance();
			String transactions = accountController.getTransactions();
			String transfer = accountController.getMoneyTransferAccountingProof("14537780");
			;
			model.addAttribute("balance", balance);
			model.addAttribute("transactions", transactions);
			model.addAttribute("transfer", transfer);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}

	@PostConstruct
	public void init() {
		performOperations();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e) {
		String errorMessage = "An error occurred while processing the request: ";

		if (e instanceof IOException) {
			errorMessage += "Error reading or writing data.";
			log.error(errorMessage, e);
		} else if (e instanceof HttpClientErrorException) {
			errorMessage += "HTTP client error.";
			log.error(errorMessage, e);
		} else if (e instanceof IllegalArgumentException) {
			errorMessage += "Invalid data provided.";
			log.error(errorMessage, e);
		} else {
			errorMessage += "Unknown error.";
			log.error(errorMessage, e);
		}

		return errorMessage;
	}

}
