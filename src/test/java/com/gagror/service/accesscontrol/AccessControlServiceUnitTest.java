package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;
import com.gagror.data.account.LoginCredentialsInput;
import com.gagror.data.account.RegisterInput;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlServiceUnitTest {

	private static final String USERNAME = "user";
	private static final String PASSWORD = "pass";
	private static final String SALT = "salt";
	private static final String ENCRYPTED = "encrypted";
	private static final String WRONG_PASSWORD = "wrongpass";
	private static final String WRONG_ENCRYPTED = "wrongencrypted";

	@Mock
	AccountRepository accountRepository;

	@Mock
	PasswordEncryptionService passwordEncryption;

	SessionCredentialsComponent sessionCredentials = new SessionCredentialsComponent();

	RequestAccountComponent requestAccount = new RequestAccountComponent();

	AccessControlService instance;

	RegisterInput registerForm = new RegisterInput();
	LoginCredentialsInput loginCredentials = new LoginCredentialsInput();
	AccountEntity account = new AccountEntity();

	// TODO Unit tests for methods to get request account (entity and output)

	@Test
	public void logIn_ok() {
		final AccessControlResultType result = instance.logIn(loginCredentials);
		assertSame("Login failed", AccessControlResultType.LOGGED_IN, result);
		assertTrue("Request account not loaded", requestAccount.isLoaded());
		assertSame("Wrong request account loaded", account, requestAccount.getAccount());
	}

	@Test
	public void logIn_wrongPassword() {
		loginCredentials.setPassword(WRONG_PASSWORD);
		final AccessControlResultType result = instance.logIn(loginCredentials);
		assertSame("Login should fail", AccessControlResultType.LOGIN_FAILED, result);
		assertTrue("Request account not loaded", requestAccount.isLoaded());
		assertNull("Wrong request account loaded", requestAccount.getAccount());
	}

	@Test
	public void logIn_accountNotFound() {
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		final AccessControlResultType result = instance.logIn(loginCredentials);
		assertSame("Login should fail", AccessControlResultType.LOGIN_FAILED, result);
		assertTrue("Request account not loaded", requestAccount.isLoaded());
		assertNull("Wrong request account loaded", requestAccount.getAccount());
	}

	@Test
	public void logIn_noCredentials() {
		final AccessControlResultType result = instance.logIn(null);
		assertSame("Login should fail", AccessControlResultType.LOGIN_FAILED, result);
		assertTrue("Request account not loaded", requestAccount.isLoaded());
		assertNull("Wrong request account loaded", requestAccount.getAccount());
	}

	@Test
	public void logOut() {
		instance.logOut();
		assertNull("Session credentials not removed", sessionCredentials.getLoginCredentials());
		assertFalse("Request account still loaded", requestAccount.isLoaded());
		verifyNoMoreInteractions(
				accountRepository,
				passwordEncryption);
	}

	@Test
	public void register_ok_loggedIn() {
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		final AccessControlResultType result = instance.register(registerForm);
		assertEquals("Wrong registration result", AccessControlResultType.LOGGED_IN, result);
		final ArgumentCaptor<AccountEntity> savedAccount = ArgumentCaptor.forClass(AccountEntity.class);
		verify(accountRepository).save(savedAccount.capture());
		assertEquals("Wrong username", USERNAME, savedAccount.getValue().getUsername());
		assertEquals("Wrong password", ENCRYPTED, savedAccount.getValue().getPassword());
		assertEquals("Wrong salt", SALT, savedAccount.getValue().getSalt());
		assertSame("Wrong account type", AccountType.STANDARD, savedAccount.getValue().getAccountType());
		assertTrue("Request account not loaded", requestAccount.isLoaded());
		assertSame("Wrong request account saved", savedAccount.getValue(), requestAccount.getAccount());
	}

	@Test
	public void register_usernameBusy() {
		final AccessControlResultType result = instance.register(registerForm);
		assertEquals("Wrong registration result", AccessControlResultType.REGISTER_FAILED_USERNAME_BUSY, result);
	}

	@Test
	public void register_passwordsDontMatch() {
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		registerForm.setPasswordRepeat("doesn't match");
		final AccessControlResultType result = instance.register(registerForm);
		assertEquals("Wrong registration result", AccessControlResultType.REGISTER_FAILED_PASSWORDS_DONT_MATCH, result);
	}

	@Before
	public void setupRegisterForm() {
		registerForm.setUsername(USERNAME);
		registerForm.setPassword(PASSWORD);
		registerForm.setPasswordRepeat(PASSWORD);
	}

	@Before
	public void setupLoginCredentials() {
		loginCredentials.setUsername(USERNAME);
		loginCredentials.setPassword(PASSWORD);
	}

	@Before
	public void setupAccount() {
		account.setUsername(USERNAME);
		account.setPassword(ENCRYPTED);
		account.setSalt(SALT);
	}

	@SuppressWarnings("rawtypes")
	@Before
	public void setupPasswordEncryption() {
		when(passwordEncryption.encrypt(any(LoginCredentialsInput.class))).then(new Answer(){
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final LoginCredentialsInput input = (LoginCredentialsInput) invocation.getArguments()[0];
				input.setSalt(SALT);
				if(null != input.getEncryptedPassword()) {
					// Already encrypted, do nothing
				} else if(PASSWORD.equals(input.getPassword())) {
					input.setEncryptedPassword(ENCRYPTED);
				} else {
					input.setEncryptedPassword(WRONG_ENCRYPTED);
				}
				return input.getEncryptedPassword();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	@Before
	public void setupAccountRepository() {
		when(accountRepository.findByUsername(USERNAME)).thenReturn(account);
		when(accountRepository.save(any(AccountEntity.class))).thenAnswer(new Answer(){
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final AccountEntity input = (AccountEntity)invocation.getArguments()[0];
				when(accountRepository.findByUsername(input.getUsername())).thenReturn(input);
				return input;
			}
		});
	}

	@Before
	public void setupInstance() {
		instance = new AccessControlService();
		instance.accountRepository = accountRepository;
		instance.passwordEncryption = passwordEncryption;
		instance.sessionCredentials = sessionCredentials;
		instance.requestAccount = requestAccount;
	}
}
