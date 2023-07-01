package com.cer.gabriel.sere;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApplicationTests {

	private AccountRestController accountController = mock(AccountRestController.class);
	private Application application = new Application(accountController);

	private Model model = mock(Model.class);

	@Test
	void testShowAccountInformation() throws Exception {
		String balance = "1000.00";
		when(accountController.getBalance()).thenReturn(balance);

		String transactions = "[{\"id\":\"1\", \"amount\":\"50.00\"}, {\"id\":\"2\", \"amount\":\"30.00\"}]";
		when(accountController.getTransactions()).thenReturn(transactions);

		String transfer = "{\"id\":\"12345\", \"amount\":\"100.00\"}";
		when(accountController.getMoneyTransferAccountingProof("14537780")).thenReturn(transfer);

		String viewName = application.showAccountInformation(model);
		assertThat(viewName).isEqualTo("index");

		verify(model).addAttribute("balance", balance);
		verify(model).addAttribute("transactions", transactions);
		verify(model).addAttribute("transfer", transfer);
	}

	@Test
    void testShowAccountInformationError() throws Exception {
        when(accountController.getBalance()).thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));

        String viewName = application.showAccountInformation(model);

        assertThat(viewName).isEqualTo("error");
        verify(model).addAttribute("errorMessage", "An error occurred while processing the request: HTTP client error.");
    }
}
