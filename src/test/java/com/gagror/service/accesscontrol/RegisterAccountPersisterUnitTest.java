package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;
import com.gagror.data.account.RegisterInput;

@RunWith(MockitoJUnitRunner.class)
public class RegisterAccountPersisterUnitTest {

	private static final String USERNAME = "user";
	private static final String PASSWORD = "password";
	private static final String PASSWORD_ENCODED = "encoded";

	RegisterAccountPersister instance;

	@Mock
	AccountRepository accountRepository;

	@Mock
	AccessControlService accessControlService;

	@Mock
	RegisterInput registerForm;

	@Mock
	BindingResult bindingResult;

	@Mock
	AccountEntity anAccount;

	@Test
	public void register_ok() {
		final boolean result = instance.save(registerForm, bindingResult);
		assertTrue("Saving should have been successful", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<AccountEntity> savedAccount = ArgumentCaptor.forClass(AccountEntity.class);
		verify(accountRepository).save(savedAccount.capture());
		assertEquals("Wrong username", USERNAME, savedAccount.getValue().getUsername());
		assertEquals("Wrong password", PASSWORD_ENCODED, savedAccount.getValue().getPassword());
		assertSame("Wrong account type", AccountType.STANDARD, savedAccount.getValue().getAccountType());
		assertTrue("Registered account should become active", savedAccount.getValue().isActive());
		assertFalse("Registered account should not become locked", savedAccount.getValue().isLocked());
		verify(accessControlService).logInAs(savedAccount.getValue());
	}

	@Test
	public void register_usernameBusy() {
		when(accountRepository.findByUsername(USERNAME)).thenReturn(anAccount);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(registerForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(registerForm).addErrorUsernameBusy(bindingResult);
		verify(accountRepository, never()).save(any(AccountEntity.class));
	}

	@Test
	public void register_passwordsDontMatch() {
		when(registerForm.getPasswordRepeat()).thenReturn("doesn't match");
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(registerForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(registerForm).addErrorPasswordMismatch(bindingResult);
		verify(accountRepository, never()).save(any(AccountEntity.class));
	}

	@Test
	public void register_passwordTooWeak() {
		when(accessControlService.isPasswordTooWeak(PASSWORD)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(registerForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(registerForm).addErrorPasswordTooWeak(bindingResult);
		verify(accountRepository, never()).save(any(AccountEntity.class));
	}

	@Test
	public void register_manyErrors() {
		when(accountRepository.findByUsername(USERNAME)).thenReturn(anAccount);
		when(registerForm.getPasswordRepeat()).thenReturn("doesn't match");
		when(accessControlService.isPasswordTooWeak(PASSWORD)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(registerForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(registerForm).addErrorUsernameBusy(bindingResult);
		verify(registerForm).addErrorPasswordMismatch(bindingResult);
		verify(registerForm).addErrorPasswordTooWeak(bindingResult);
		verify(accountRepository, never()).save(any(AccountEntity.class));
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.encodePassword(PASSWORD)).thenReturn(PASSWORD_ENCODED);
	}

	@Before
	public void setupRegisterForm() {
		when(registerForm.getUsername()).thenReturn(USERNAME);
		when(registerForm.getPassword()).thenReturn(PASSWORD);
		when(registerForm.getPasswordRepeat()).thenReturn(PASSWORD);
	}

	@Before
	public void setupInstance() {
		instance = new RegisterAccountPersister();
		instance.accountRepository = accountRepository;
		instance.accessControlService = accessControlService;
	}
}
