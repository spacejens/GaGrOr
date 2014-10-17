package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;
import com.gagror.data.account.RegisterInput;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlServiceUnitTest {

	private static final String USERNAME = "user";
	private static final String PASSWORD = "pass";

	@Mock
	AccountRepository accountRepository;

	RequestAccountComponent requestAccount = new RequestAccountComponent();

	AccessControlService instance;

	@Mock
	RegisterInput registerForm;

	@Mock
	BindingResult bindingResult;

	AccountEntity account = new AccountEntity();

	@Test
	public void loadAccountEntity_ok() {
		assertSame("Unexpected account loaded", account, instance.loadAccountEntity(mockValidAuthentication()));
	}

	@Test
	public void loadAccountEntity_noAuthentication() {
		assertNull("No account should be loaded without authentication", instance.loadAccountEntity(null));
	}

	@Test
	public void loadAccountEntity_notAuthenticated() {
		final Authentication auth = mockValidAuthentication();
		when(auth.isAuthenticated()).thenReturn(false);
		assertNull("No account should be loaded when not authenticated", instance.loadAccountEntity(auth));
	}

	@Test
	public void loadAccountEntity_anonymous() {
		assertNull("No account should be loaded when anonymous", instance.loadAccountEntity(getAnonymousAuthentication()));
	}

	@Test
	public void loadAccountEntity_accountNotFound() {
		final Authentication auth = mockValidAuthentication();
		when(auth.getName()).thenReturn("UnknownUser");
		assertNull("No account should be loaded for unknown name", instance.loadAccountEntity(auth));
	}

	@Test
	public void getRequestAccountEntity_ok() {
		whenLoggedIn();
		final AccountEntity result = instance.getRequestAccountEntity();
		assertSame("Failed to get request account", account, result);
		assertTrue("Should have marked request account as loaded", requestAccount.isLoaded());
		assertSame("Should have cached request account", account, requestAccount.getAccount());
	}

	@Test
	public void getRequestAccountEntity_notLoggedIn() {
		whenNotLoggedIn();
		final AccountEntity result = instance.getRequestAccountEntity();
		assertNull("Should not have loaded request account", result);
		assertTrue("Should have marked request account as loaded", requestAccount.isLoaded());
		assertNull("Should have cached request account null", requestAccount.getAccount());
	}

	@Test
	public void getRequestAccountEntity_accountNotFound() {
		whenLoggedIn();
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		final AccountEntity result = instance.getRequestAccountEntity();
		assertNull("Should not have loaded request account", result);
		assertTrue("Should have marked request account as loaded", requestAccount.isLoaded());
		assertNull("Should have cached request account null", requestAccount.getAccount());
	}

	@Test
	public void getRequestAccount_ok() {
		whenLoggedIn();
		final AccountReferenceOutput result = instance.getRequestAccount();
		assertEquals("Wrong ID", account.getId(), result.getId());
		assertEquals("Wrong username", account.getUsername(), result.getUsername());
		assertEquals("Wrong account type", account.getAccountType(), result.getAccountType());
		assertTrue("Should have marked request account as loaded", requestAccount.isLoaded());
		assertSame("Should have cached request account", account, requestAccount.getAccount());
	}

	@Test
	public void getRequestAccount_notLoggedIn() {
		whenNotLoggedIn();
		final AccountReferenceOutput result = instance.getRequestAccount();
		assertNull("Should not have loaded request account", result);
		assertTrue("Should have marked request account as loaded", requestAccount.isLoaded());
		assertNull("Should have cached request account null", requestAccount.getAccount());
	}

	@Test
	public void getRequestAccount_accountNotFound() {
		whenLoggedIn();
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		final AccountReferenceOutput result = instance.getRequestAccount();
		assertNull("Should not have loaded request account", result);
		assertTrue("Should have marked request account as loaded", requestAccount.isLoaded());
		assertNull("Should have cached request account null", requestAccount.getAccount());
	}

	@Test
	public void register_ok() {
		whenNotLoggedIn();
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		instance.register(registerForm, bindingResult);
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<AccountEntity> savedAccount = ArgumentCaptor.forClass(AccountEntity.class);
		verify(accountRepository).save(savedAccount.capture());
		assertEquals("Wrong username", USERNAME, savedAccount.getValue().getUsername());
		assertEquals("Wrong password", PASSWORD, savedAccount.getValue().getPassword());
		assertSame("Wrong account type", AccountType.STANDARD, savedAccount.getValue().getAccountType());
		assertNotNull("Authentication not set in security context", SecurityContextHolder.getContext().getAuthentication());
		assertEquals("Not logged in as correct user after registration", USERNAME, SecurityContextHolder.getContext().getAuthentication().getName());
		assertTrue("Not authenticated after registration", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
	}

	@Test
	public void register_usernameBusy() {
		whenNotLoggedIn();
		instance.register(registerForm, bindingResult);
		verify(registerForm).addErrorUsernameBusy(bindingResult);
		verify(accountRepository, never()).save(any(AccountEntity.class));
	}

	@Test
	public void register_passwordsDontMatch() {
		whenNotLoggedIn();
		when(accountRepository.findByUsername(USERNAME)).thenReturn(null);
		when(registerForm.getPasswordRepeat()).thenReturn("doesn't match");
		instance.register(registerForm, bindingResult);
		verify(registerForm).addErrorPasswordMismatch(bindingResult);
		verify(accountRepository, never()).save(any(AccountEntity.class));
	}

	protected void whenNotLoggedIn() {
		SecurityContextHolder.getContext().setAuthentication(getAnonymousAuthentication());
	}

	protected Authentication getAnonymousAuthentication() {
		final Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
		return new AnonymousAuthenticationToken("key", "anonymousUser", authorities);
	}

	protected void whenLoggedIn() {
		SecurityContextHolder.getContext().setAuthentication(mockValidAuthentication());
	}

	protected Authentication mockValidAuthentication() {
		final Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn(USERNAME);
		when(authentication.isAuthenticated()).thenReturn(true);
		return authentication;
	}

	@Before
	public void setupRegisterForm() {
		when(registerForm.getUsername()).thenReturn(USERNAME);
		when(registerForm.getPassword()).thenReturn(PASSWORD);
		when(registerForm.getPasswordRepeat()).thenReturn(PASSWORD);
	}

	@Before
	public void setupAccount() {
		account.setUsername(USERNAME);
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
		instance.requestAccount = requestAccount;
		instance.passwordEncoder = NoOpPasswordEncoder.getInstance();
	}
}
