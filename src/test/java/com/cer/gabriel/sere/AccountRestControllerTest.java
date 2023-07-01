package com.cer.gabriel.sere;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
class AccountRestControllerTests {

	private AccountRestController accountRestController;

	@BeforeEach
	void setUp() {
		accountRestController = new AccountRestController();
	}

	@Test
	void testGetBalance() throws IOException {
		String compactJson = accountRestController.getBalance();

		assertNotNull(compactJson);
		assertFalse(compactJson.isEmpty());
	}

	@Test
	void testGetTransactions() throws IOException {
		String compactJson = accountRestController.getTransactions();

		assertNotNull(compactJson);
		assertFalse(compactJson.isEmpty());
	}

	@Test
	void testGetMoneyTransferAccountingProof() throws IOException {
		String accountId = "123456"; // Replace with the actual account ID
		String compactJson = accountRestController.getMoneyTransferAccountingProof(accountId);

		assertNotNull(compactJson);
		assertFalse(compactJson.isEmpty());
	}

	@Test
	void testHandleException_IOException() {
		Exception e = new IOException("Error reading or writing data.");
		String errorMessage = accountRestController.handleException(e);

		assertNotNull(errorMessage);
		assertTrue(errorMessage.contains("An error occurred while processing the request"));
		assertTrue(errorMessage.contains("Error reading or writing data."));
	}

	@Test
	void testHandleException_HttpClientErrorException() {
		Exception e = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
		String errorMessage = accountRestController.handleException(e);

		assertNotNull(errorMessage);
		assertTrue(errorMessage.contains("An error occurred while processing the request"));
		assertTrue(errorMessage.contains("HTTP client error."));
	}

	@Test
	void testHandleException_IllegalArgumentException() {
		Exception e = new IllegalArgumentException("Invalid data provided.");
		String errorMessage = accountRestController.handleException(e);

		assertNotNull(errorMessage);
		assertTrue(errorMessage.contains("An error occurred while processing the request"));
		assertTrue(errorMessage.contains("Invalid data provided."));
	}

	@Test
	void testHandleException_UnknownError() {
		Exception e = new RuntimeException("Unknown error.");
		String errorMessage = accountRestController.handleException(e);

		assertNotNull(errorMessage);
		assertTrue(errorMessage.contains("An error occurred while processing the request"));
		assertTrue(errorMessage.contains("Unknown error."));
	}
}
