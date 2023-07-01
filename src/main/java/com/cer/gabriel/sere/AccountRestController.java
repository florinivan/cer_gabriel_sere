package com.cer.gabriel.sere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping("/account")
public class AccountRestController {

	private final HttpClient httpClient = HttpClients.createDefault();
	private static final Log log = LogFactory.getLog(AccountRestController.class);

	@Autowired
	private OperationRep operationRep;

	@GetMapping("/balance")
	public String getBalance() throws IOException {
		String url = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/14537780/balance";
		HttpGet request = new HttpGet(url);
		request.setHeader("Auth-Schema", "S2S");
		request.setHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		HttpResponse response = httpClient.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuilder result = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		Object json = objectMapper.readValue(result.toString(), Object.class);
		String compactJson = objectMapper.writeValueAsString(json);

		LocalDateTime dateTime = LocalDateTime.now();
		OperationToDB operation = new OperationToDB("Verificare balanta", dateTime);
		operationRep.save(operation);

		return compactJson;
	}

	@GetMapping("/transactions")
	public String getTransactions() throws IOException {
		String url = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/{accountId}/transactions";
		String accountId = "14537780";
		String fromAccountingDate = "2019-04-01";
		String toAccountingDate = "2020-04-01";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				.queryParam("fromAccountingDate", fromAccountingDate).queryParam("toAccountingDate", toAccountingDate);

		url = builder.buildAndExpand(accountId).toUriString();

		HttpGet request = new HttpGet(url);
		request.setHeader("Auth-Schema", "S2S");
		request.setHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		HttpResponse response = httpClient.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuilder result = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}

		Object json = objectMapper.readValue(result.toString(), Object.class);
		String compactJson = objectMapper.writeValueAsString(json);

		LocalDateTime dateTime = LocalDateTime.now();
		OperationToDB operation = new OperationToDB("Verificare tranzactii", dateTime);
		operationRep.save(operation);

		return compactJson;
	}

	@GetMapping("/accounting-proof/{accountId}")
	public String getMoneyTransferAccountingProof(@PathVariable String accountId) throws IOException {
		String url = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers/{moneyTransferId}/accounting-proof";
		String moneyTransferId = "123456"; // Replace with the actual money transfer ID

		url = url.replace("{accountId}", accountId).replace("{moneyTransferId}", moneyTransferId);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

		UriComponents uriComponents = builder.buildAndExpand(accountId, moneyTransferId);

		url = uriComponents.toUriString();

		HttpGet request = new HttpGet(url);
		request.setHeader("Auth-Schema", "S2S");
		request.setHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		HttpResponse response = httpClient.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuilder result = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}

		Object json = objectMapper.readValue(result.toString(), Object.class);
		String compactJson = objectMapper.writeValueAsString(json);

		LocalDateTime dateTime = LocalDateTime.now();
		OperationToDB operation = new OperationToDB("Verificare stare transfer", dateTime);
		operationRep.save(operation);

		return compactJson;
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
