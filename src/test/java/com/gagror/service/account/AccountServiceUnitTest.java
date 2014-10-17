package com.gagror.service.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

	private static final Long ACCOUNT_ID = 47L;
	private static final Long ANOTHER_ID = 23L;

	private static final Long VERSION = 3L;

	private static final String FORM_USERNAME = "NewUsername";

	AccountService instance;

	@Mock
	AccountRepository accountRepository;

	@Mock
	AccountEntity account;

	AccountEditInput editAccountForm;

	@Mock
	BindingResult bindingResult;

	@Test
	public void loadAccountForEditing_ok() {
		final AccountEditOutput result = instance.loadAccountForEditing(ACCOUNT_ID);
		assertNotNull("Failed to load account", result);
		assertEquals("Unexpected account loaded", ACCOUNT_ID, result.getId());
	}

	@Test
	public void loadAccountForEditing_notFound() {
		final AccountEditOutput result = instance.loadAccountForEditing(ANOTHER_ID);
		assertNull("Should not have found any account", result);
	}

	@Test
	public void saveAccount_ok() {
		instance.saveAccount(editAccountForm, bindingResult);
		verify(account).setUsername(FORM_USERNAME);
	}

	@Test(expected=IllegalStateException.class)
	public void saveAccount_simultaneousEdit() {
		when(account.getVersion()).thenReturn(VERSION+1);
		instance.saveAccount(editAccountForm, bindingResult);
	}

	@Test(expected=IllegalStateException.class)
	public void saveAccount_accountNotFound() {
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(null);
		instance.saveAccount(editAccountForm, bindingResult);
	}

	@Before
	public void setupEditAccountForm() {
		editAccountForm = new AccountEditInput();
		editAccountForm.setId(ACCOUNT_ID);
		editAccountForm.setVersion(VERSION);
		editAccountForm.setUsername(FORM_USERNAME);
	}

	@Before
	public void setupAccount() {
		when(account.getId()).thenReturn(ACCOUNT_ID);
		when(account.getVersion()).thenReturn(VERSION);
	}

	@Before
	public void setupAccountRepository() {
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(account);
	}

	@Before
	public void setupInstance() {
		instance = new AccountService();
		instance.accountRepository = accountRepository;
	}
}
