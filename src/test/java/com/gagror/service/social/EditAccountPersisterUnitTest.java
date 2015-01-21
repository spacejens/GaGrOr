package com.gagror.service.social;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class EditAccountPersisterUnitTest {

	private static final Long ACCOUNT_ID = 47L;
	private static final Long ANOTHER_ACCOUNT_ID = 23L;
	private static final Long VERSION = 3L;
	private static final String FORM_USERNAME = "NewUsername";
	private static final AccountType FORM_ACCOUNT_TYPE = AccountType.STANDARD;
	private static final boolean FORM_ACTIVE = true;
	private static final boolean FORM_LOCKED = false;
	private static final String FORM_PASSWORD = "NewPassword";
	private static final String ENCODED_PASSWORD = "EncodedPassword";
	private static final String ENTITY_USERNAME = "OldUsername";
	private static final AccountType ENTITY_ACCOUNT_TYPE = AccountType.ADMIN;

	EditAccountPersister instance;

	@Mock
	AccountRepository accountRepository;

	@Mock
	AccessControlService accessControlService;

	@Mock
	AccountEditInput editAccountForm;

	@Mock
	BindingResult bindingResult;

	@Mock
	AccountEntity account;

	@Mock
	AccountEntity anotherAccount;

	@Test
	public void saveAccount_editingOwnAccount_ok() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(account);
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		verify(account).setName(FORM_USERNAME);
		verify(account).setPassword(ENCODED_PASSWORD);
		verify(account, never()).setAccountType(any(AccountType.class));
		verify(account, never()).setActive(anyBoolean());
		verify(account, never()).setLocked(anyBoolean());
		verify(accessControlService).logInAs(account);
	}

	@Test
	public void saveAccount_editingOwnAccount_dontChangePassword_ok() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(account);
		when(editAccountForm.getPassword()).thenReturn("");
		when(editAccountForm.getPasswordRepeat()).thenReturn("");
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		verify(account).setName(FORM_USERNAME);
		verify(account, never()).setPassword(anyString());
		verify(account, never()).setAccountType(any(AccountType.class));
		verify(account, never()).setActive(anyBoolean());
		verify(account, never()).setLocked(anyBoolean());
		verify(accessControlService).logInAs(account);
	}

	@Test
	public void saveAccount_editingOtherAccount_ok() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(anotherAccount);
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		verify(account, never()).setName(FORM_USERNAME);
		verify(account).setPassword(ENCODED_PASSWORD);
		verify(account).setAccountType(FORM_ACCOUNT_TYPE);
		verify(account).setActive(FORM_ACTIVE);
		verify(account).setLocked(FORM_LOCKED);
		verify(accessControlService, never()).logInAs(account);
	}

	@Test
	public void saveAccount_editingOtherAccount_dontChangePassword_ok() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(anotherAccount);
		when(editAccountForm.getPassword()).thenReturn("");
		when(editAccountForm.getPasswordRepeat()).thenReturn("");
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		verify(account, never()).setName(FORM_USERNAME);
		verify(account, never()).setPassword(anyString());
		verify(account).setAccountType(FORM_ACCOUNT_TYPE);
		verify(account).setActive(FORM_ACTIVE);
		verify(account).setLocked(FORM_LOCKED);
		verify(accessControlService, never()).logInAs(account);
	}

	@Test
	public void saveAccount_usernameBusy() {
		when(accountRepository.findByName(FORM_USERNAME)).thenReturn(mock(AccountEntity.class));
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(editAccountForm).addErrorUsernameBusy(bindingResult);
		verify(account, never()).setName(anyString());
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_passwordMismatch() {
		when(editAccountForm.getPassword()).thenReturn("Something");
		when(editAccountForm.getPasswordRepeat()).thenReturn("Something Else");
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(editAccountForm).addErrorPasswordMismatch(bindingResult);
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_passwordTooWeak() {
		when(accessControlService.isPasswordTooWeak(FORM_PASSWORD)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(editAccountForm).addErrorPasswordTooWeak(bindingResult);
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_disallowedAccountType() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(anotherAccount); // Needed to be allowed to edit
		when(editAccountForm.getAccountType()).thenReturn(AccountType.SYSTEM_OWNER);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(editAccountForm).addErrorDisallowedAccountType(bindingResult);
		verify(account, never()).setAccountType(any(AccountType.class));
	}

	@Test
	public void saveAccount_simultaneousEdit() {
		when(account.getVersion()).thenReturn(VERSION+1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(editAccountForm).addErrorSimultaneuosEdit(bindingResult);
		verify(account, never()).setName(anyString());
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_manyErrors() {
		when(accountRepository.findByName(FORM_USERNAME)).thenReturn(mock(AccountEntity.class));
		when(editAccountForm.getPasswordRepeat()).thenReturn("Doesn't match");
		when(accessControlService.isPasswordTooWeak(FORM_PASSWORD)).thenReturn(true);
		when(account.getVersion()).thenReturn(VERSION+1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(editAccountForm, bindingResult);
		assertFalse("Saving should have failed", result);
		verify(editAccountForm).addErrorUsernameBusy(bindingResult);
		verify(editAccountForm).addErrorPasswordMismatch(bindingResult);
		verify(editAccountForm).addErrorPasswordTooWeak(bindingResult);
		verify(editAccountForm).addErrorSimultaneuosEdit(bindingResult);
		verify(account, never()).setName(anyString());
		verify(account, never()).setPassword(anyString());
		verify(account, never()).setAccountType(any(AccountType.class));
		/* Not trying to edit account type, since it can never be edited at the same time as username
		 * Account type is only editable for other accounts, username only for your own account.
		 */
	}

	@Test(expected=DataNotFoundException.class)
	public void saveAccount_accountNotFound() {
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(null);
		instance.save(editAccountForm, bindingResult);
	}

	@Before
	public void setupAccount() {
		when(account.getId()).thenReturn(ACCOUNT_ID);
		when(account.getVersion()).thenReturn(VERSION);
		when(account.getName()).thenReturn(ENTITY_USERNAME);
		when(account.getAccountType()).thenReturn(ENTITY_ACCOUNT_TYPE);
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(account);
		when(anotherAccount.getId()).thenReturn(ANOTHER_ACCOUNT_ID);
		when(anotherAccount.getAccountType()).thenReturn(ENTITY_ACCOUNT_TYPE);
	}

	@Before
	public void setupEditAccountForm() {
		when(editAccountForm.getId()).thenReturn(ACCOUNT_ID);
		when(editAccountForm.getVersion()).thenReturn(VERSION);
		when(editAccountForm.getName()).thenReturn(FORM_USERNAME);
		when(editAccountForm.getPassword()).thenReturn(FORM_PASSWORD);
		when(editAccountForm.getPasswordRepeat()).thenReturn(FORM_PASSWORD);
		when(editAccountForm.getAccountType()).thenReturn(FORM_ACCOUNT_TYPE);
		when(editAccountForm.isActive()).thenReturn(FORM_ACTIVE);
		when(editAccountForm.isLocked()).thenReturn(FORM_LOCKED);
	}

	@Before
	public void setupBindingResult() {
		when(bindingResult.getObjectName()).thenReturn("");
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(account);
		when(accessControlService.encodePassword(FORM_PASSWORD)).thenReturn(ENCODED_PASSWORD);
	}

	@Before
	public void setupInstance() {
		instance = new EditAccountPersister();
		instance.accessControlService = accessControlService;
		instance.accountRepository = accountRepository;
	}
}
