package com.gagror.service.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.account.ContactReferenceOutput;
import com.gagror.data.account.ContactType;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

	private static final Long ACCOUNT_ID = 47L;
	private static final Long CONTACT_ID = 67L;
	private static final Long ANOTHER_ID = 23L;

	private static final Long VERSION = 3L;

	private static final String ENTITY_USERNAME = "OldUsername";
	private static final AccountType ENTITY_ACCOUNT_TYPE = AccountType.ADMIN;
	private static final String FORM_USERNAME = "NewUsername";
	private static final String FORM_PASSWORD = "NewPassword";
	private static final AccountType FORM_ACCOUNT_TYPE = AccountType.STANDARD;
	private static final boolean FORM_ACTIVE = true;
	private static final boolean FORM_LOCKED = false;
	private static final String ENCODED_PASSWORD = "EncodedPassword";

	AccountService instance;

	@Mock
	AccountRepository accountRepository;

	@Mock
	AccessControlService accessControlService;

	@Mock
	AccountEntity account;

	@Mock
	ContactEntity contact;

	@Mock
	AccountEntity contactAccount;

	@Mock
	ContactEntity anotherContact;

	@Mock
	AccountEntity anotherAccount;

	@Mock
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
	public void saveAccount_editingOwnAccount_ok() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(account);
		instance.saveAccount(editAccountForm, bindingResult);
		verify(account).setUsername(FORM_USERNAME);
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
		instance.saveAccount(editAccountForm, bindingResult);
		verify(account).setUsername(FORM_USERNAME);
		verify(account, never()).setPassword(anyString());
		verify(account, never()).setAccountType(any(AccountType.class));
		verify(account, never()).setActive(anyBoolean());
		verify(account, never()).setLocked(anyBoolean());
		verify(accessControlService).logInAs(account);
	}

	@Test
	public void saveAccount_editingOtherAccount_ok() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(anotherAccount);
		instance.saveAccount(editAccountForm, bindingResult);
		verify(account, never()).setUsername(FORM_USERNAME);
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
		instance.saveAccount(editAccountForm, bindingResult);
		verify(account, never()).setUsername(FORM_USERNAME);
		verify(account, never()).setPassword(anyString());
		verify(account).setAccountType(FORM_ACCOUNT_TYPE);
		verify(account).setActive(FORM_ACTIVE);
		verify(account).setLocked(FORM_LOCKED);
		verify(accessControlService, never()).logInAs(account);
	}

	@Test
	public void saveAccount_usernameBusy() {
		when(accountRepository.findByUsername(FORM_USERNAME)).thenReturn(mock(AccountEntity.class));
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		instance.saveAccount(editAccountForm, bindingResult);
		verify(editAccountForm).addErrorUsernameBusy(bindingResult);
		verify(account, never()).setUsername(anyString());
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_passwordMismatch() {
		when(editAccountForm.getPassword()).thenReturn("Something");
		when(editAccountForm.getPasswordRepeat()).thenReturn("Something Else");
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		instance.saveAccount(editAccountForm, bindingResult);
		verify(editAccountForm).addErrorPasswordMismatch(bindingResult);
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_passwordTooWeak() {
		when(accessControlService.isPasswordTooWeak(FORM_PASSWORD)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		instance.saveAccount(editAccountForm, bindingResult);
		verify(editAccountForm).addErrorPasswordTooWeak(bindingResult);
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_disallowedAccountType() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(anotherAccount); // Needed to be allowed to edit
		when(editAccountForm.getAccountType()).thenReturn(AccountType.SYSTEM_OWNER);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		instance.saveAccount(editAccountForm, bindingResult);
		verify(editAccountForm).addErrorDisallowedAccountType(bindingResult);
		verify(account, never()).setAccountType(any(AccountType.class));
	}

	@Test
	public void saveAccount_simultaneousEdit() {
		when(account.getVersion()).thenReturn(VERSION+1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		instance.saveAccount(editAccountForm, bindingResult);
		verify(editAccountForm).addErrorSimultaneuosEdit(bindingResult);
		verify(account, never()).setUsername(anyString());
		verify(account, never()).setPassword(anyString());
	}

	@Test
	public void saveAccount_manyErrors() {
		when(accountRepository.findByUsername(FORM_USERNAME)).thenReturn(mock(AccountEntity.class));
		when(editAccountForm.getPasswordRepeat()).thenReturn("Doesn't match");
		when(accessControlService.isPasswordTooWeak(FORM_PASSWORD)).thenReturn(true);
		when(account.getVersion()).thenReturn(VERSION+1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		instance.saveAccount(editAccountForm, bindingResult);
		verify(editAccountForm).addErrorUsernameBusy(bindingResult);
		verify(editAccountForm).addErrorPasswordMismatch(bindingResult);
		verify(editAccountForm).addErrorPasswordTooWeak(bindingResult);
		verify(editAccountForm).addErrorSimultaneuosEdit(bindingResult);
		verify(account, never()).setUsername(anyString());
		verify(account, never()).setPassword(anyString());
		verify(account, never()).setAccountType(any(AccountType.class));
		/* Not trying to edit account type, since it can never be edited at the same time as username
		 * Account type is only editable for other accounts, username only for your own account.
		 */
	}

	@Test(expected=IllegalStateException.class)
	public void saveAccount_accountNotFound() {
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(null);
		instance.saveAccount(editAccountForm, bindingResult);
	}

	@Test
	public void loadContacts() {
		final List<ContactReferenceOutput> contacts = instance.loadContacts();
		assertContactAccountIDs(contacts, CONTACT_ID);
	}

	@Test
	public void loadSentContactRequests() {
		final List<ContactReferenceOutput> contacts = instance.loadSentContactRequests();
		assertContactAccountIDs(contacts, ANOTHER_ID);
	}

	@Test
	public void loadReceivedContactRequests() {
		when(anotherContact.getOwner()).thenReturn(anotherAccount);
		when(anotherContact.getContact()).thenReturn(account);
		account.getContacts().remove(anotherContact);
		account.getIncomingContacts().add(anotherContact);
		final List<ContactReferenceOutput> contacts = instance.loadReceivedContactRequests();
		assertContactAccountIDs(contacts, ANOTHER_ID);
	}

	private void assertContactAccountIDs(final List<ContactReferenceOutput> contacts, final Long... accountIDs) {
		final Set<Long> expected = new HashSet<>();
		expected.addAll(Arrays.asList(accountIDs));
		final Set<Long> actual = new HashSet<>();
		for(final ContactReferenceOutput contact : contacts) {
			actual.add(contact.getId());
		}
		assertEquals("Unexpected account IDs in contacts", expected, actual);
	}

	@Before
	public void setupBindingResult() {
		when(bindingResult.getObjectName()).thenReturn("");
	}

	@Before
	public void setupEditAccountForm() {
		when(editAccountForm.getId()).thenReturn(ACCOUNT_ID);
		when(editAccountForm.getVersion()).thenReturn(VERSION);
		when(editAccountForm.getUsername()).thenReturn(FORM_USERNAME);
		when(editAccountForm.getPassword()).thenReturn(FORM_PASSWORD);
		when(editAccountForm.getPasswordRepeat()).thenReturn(FORM_PASSWORD);
		when(editAccountForm.getAccountType()).thenReturn(FORM_ACCOUNT_TYPE);
		when(editAccountForm.isActive()).thenReturn(FORM_ACTIVE);
		when(editAccountForm.isLocked()).thenReturn(FORM_LOCKED);
	}

	@Before
	public void setupAccount() {
		// Set up accounts
		when(account.getId()).thenReturn(ACCOUNT_ID);
		when(account.getVersion()).thenReturn(VERSION);
		when(account.getUsername()).thenReturn(ENTITY_USERNAME);
		when(account.getAccountType()).thenReturn(ENTITY_ACCOUNT_TYPE);
		when(contactAccount.getId()).thenReturn(CONTACT_ID);
		when(anotherAccount.getId()).thenReturn(ANOTHER_ID);
		when(anotherAccount.getAccountType()).thenReturn(ENTITY_ACCOUNT_TYPE);
		// Set up a contact
		when(contact.getOwner()).thenReturn(account);
		when(contact.getContact()).thenReturn(contactAccount);
		when(contact.getContactType()).thenReturn(ContactType.APPROVED);
		// Set up another contact
		when(anotherContact.getOwner()).thenReturn(account);
		when(anotherContact.getContact()).thenReturn(anotherAccount);
		when(anotherContact.getContactType()).thenReturn(ContactType.REQUESTED);
		// Set up contact collection
		final Set<ContactEntity> contacts = new HashSet<>();
		contacts.add(contact);
		contacts.add(anotherContact);
		when(account.getContacts()).thenReturn(contacts);
		final Set<ContactEntity> incomingContacts = new HashSet<>();
		when(account.getIncomingContacts()).thenReturn(incomingContacts);
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(account);
		when(accessControlService.encodePassword(FORM_PASSWORD)).thenReturn(ENCODED_PASSWORD);
	}

	@Before
	public void setupAccountRepository() {
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(account);
		final List<AccountEntity> allAccounts = new ArrayList<>();
		allAccounts.add(account);
		allAccounts.add(anotherAccount);
		when(accountRepository.findAll(any(Sort.class))).thenReturn(allAccounts);
	}

	@Before
	public void setupInstance() {
		instance = new AccountService();
		instance.accountRepository = accountRepository;
		instance.accessControlService = accessControlService;
	}
}
