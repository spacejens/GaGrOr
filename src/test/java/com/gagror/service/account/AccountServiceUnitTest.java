package com.gagror.service.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.account.ContactReferenceOutput;
import com.gagror.data.account.ContactRepository;
import com.gagror.data.account.ContactType;
import com.gagror.data.account.ContactViewOutput;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

	private static final Long ACCOUNT_ID = 47L;
	private static final Long CONTACT_ACCOUNT_ID = 67L;
	private static final Long ANOTHER_ACCOUNT_ID = 23L;
	private static final Long CONTACT_ID = 111L;
	private static final Long ANOTHER_CONTACT_ID = 222L;

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
	List<AccountEntity> allAccounts;

	@Mock
	ContactRepository contactRepository;

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
	public void loginAsUser_ok() {
		instance.loginAsUser(ACCOUNT_ID);
		verify(accessControlService).logInAs(account);
	}

	@Test
	public void loginAsUser_notFound() {
		when(accountRepository.findById(ANOTHER_ACCOUNT_ID)).thenReturn(null);
		instance.loginAsUser(ANOTHER_ACCOUNT_ID);
		verify(accessControlService, never()).logInAs(any(AccountEntity.class));
	}

	@Test
	public void loadAccountForEditing_ok() {
		final AccountEditOutput result = instance.loadAccountForEditing(ACCOUNT_ID);
		assertNotNull("Failed to load account", result);
		assertEquals("Unexpected account loaded", ACCOUNT_ID, result.getId());
	}

	@Test
	public void loadAccountForEditing_notFound() {
		when(accountRepository.findById(ANOTHER_ACCOUNT_ID)).thenReturn(null);
		final AccountEditOutput result = instance.loadAccountForEditing(ANOTHER_ACCOUNT_ID);
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
		// Add name to the first contact
		when(contactAccount.getUsername()).thenReturn("ZZZ");
		// Add a second contact (needed to verify sort order)
		final AccountEntity secondContactAccount = mock(AccountEntity.class);
		final Long secondContactId = 555L;
		when(secondContactAccount.getId()).thenReturn(secondContactId);
		when(secondContactAccount.getUsername()).thenReturn("AAA");
		final ContactEntity secondContact = mock(ContactEntity.class);
		when(secondContact.getContactType()).thenReturn(ContactType.APPROVED);
		when(secondContact.getContact()).thenReturn(secondContactAccount);
		account.getContacts().add(secondContact);
		// Call the method and verify the results
		final List<ContactReferenceOutput> contacts = instance.loadContacts();
		assertContactAccountIDs(contacts, secondContactId, CONTACT_ACCOUNT_ID);
	}

	@Test
	public void loadSentContactRequests() {
		// Add name to the first sent contact request
		when(anotherAccount.getUsername()).thenReturn("ZZZ");
		// Add a second contact request (needed to verify sort order)
		final AccountEntity secondContactAccount = mock(AccountEntity.class);
		final Long secondContactId = 555L;
		when(secondContactAccount.getId()).thenReturn(secondContactId);
		when(secondContactAccount.getUsername()).thenReturn("AAA");
		final ContactEntity secondContact = mock(ContactEntity.class);
		when(secondContact.getContactType()).thenReturn(ContactType.REQUESTED);
		when(secondContact.getContact()).thenReturn(secondContactAccount);
		account.getContacts().add(secondContact);
		// Call the method and verify the results
		final List<ContactReferenceOutput> contacts = instance.loadSentContactRequests();
		assertContactAccountIDs(contacts, secondContactId, ANOTHER_ACCOUNT_ID);
	}

	@Test
	public void loadReceivedContactRequests() {
		// Redefine the default test data
		when(anotherAccount.getUsername()).thenReturn("ZZZ");
		when(anotherContact.getOwner()).thenReturn(anotherAccount);
		when(anotherContact.getContact()).thenReturn(account);
		account.getContacts().remove(anotherContact);
		account.getIncomingContacts().add(anotherContact);
		// Add a second contact request (needed to verify sort order)
		final AccountEntity secondIncomingRequestAccount = mock(AccountEntity.class);
		final Long secondIncomingRequestAccountId = 555L;
		when(secondIncomingRequestAccount.getId()).thenReturn(secondIncomingRequestAccountId);
		when(secondIncomingRequestAccount.getUsername()).thenReturn("AAA");
		final ContactEntity secondIncomingRequest = mock(ContactEntity.class);
		when(secondIncomingRequest.getContactType()).thenReturn(ContactType.REQUESTED);
		when(secondIncomingRequest.getOwner()).thenReturn(secondIncomingRequestAccount);
		account.getIncomingContacts().add(secondIncomingRequest);
		// Call the method and verify the results
		final List<ContactReferenceOutput> contacts = instance.loadReceivedContactRequests();
		assertContactAccountIDs(contacts, secondIncomingRequestAccountId, ANOTHER_ACCOUNT_ID);
	}

	@Test
	public void loadAccountsNotContacts() {
		// Own account is added by default test setup (and will be filtered out)
		// Contact account is added by default test setup (and will be filtered out)
		// Add an incoming request account (that will be filtered out)
		final AccountEntity incomingAccount = mock(AccountEntity.class);
		when(incomingAccount.getId()).thenReturn(123L);
		allAccounts.add(incomingAccount);
		final ContactEntity incomingContact = mock(ContactEntity.class);
		when(incomingContact.getOwner()).thenReturn(incomingAccount);
		account.getIncomingContacts().add(incomingContact);
		// Add two non-contact accounts
		final AccountEntity firstNonContact = mock(AccountEntity.class);
		final Long firstId = 555L;
		when(firstNonContact.getId()).thenReturn(firstId);
		allAccounts.add(firstNonContact);
		final AccountEntity secondNonContact = mock(AccountEntity.class);
		final Long secondId = 777L;
		when(secondNonContact.getId()).thenReturn(secondId);
		allAccounts.add(secondNonContact);
		// Call the method and verify the results
		final List<ContactReferenceOutput> contacts = instance.loadAccountsNotContacts();
		assertContactAccountIDs(contacts, firstId, secondId);
		// Verify that sorting occurred on the database level (since that is what test data setup is based on)
		final ArgumentCaptor<Sort> sort = ArgumentCaptor.forClass(Sort.class);
		verify(accountRepository).findAll(sort.capture());
		final Iterator<Order> orderIterator = sort.getValue().iterator();
		assertTrue("Missing sort order", orderIterator.hasNext());
		assertEquals("Unexpected sort order", "username", orderIterator.next().getProperty());
		assertFalse("Should only sort on username", orderIterator.hasNext());
	}

	@Test
	public void loadContact_ok() {
		final ContactViewOutput result = instance.loadContact(CONTACT_ID);
		assertNotNull("Contact should have been loaded", result);
		assertEquals("Unexpected contact loaded", CONTACT_ID, result.getContactId());
	}

	@Test
	public void loadContact_notFound() {
		final ContactViewOutput result = instance.loadContact(9574L);
		assertNull("Contact should not have been loaded", result);
	}

	@Test
	public void createContactRequest_ok() {
		account.getContacts().remove(contact);
		final int sizeBefore = account.getContacts().size();
		final int incomingSizeBefore = contactAccount.getIncomingContacts().size();
		instance.createContactRequest(CONTACT_ACCOUNT_ID);
		final ArgumentCaptor<ContactEntity> saved = ArgumentCaptor.forClass(ContactEntity.class);
		verify(contactRepository).save(saved.capture());
		assertSame("Unexpected owner of created contact", account, saved.getValue().getOwner());
		assertSame("Unexpected contact of created contact", contactAccount, saved.getValue().getContact());
		assertSame("Unexpected contact type of created contact", ContactType.REQUESTED, saved.getValue().getContactType());
		final int sizeAfter = account.getContacts().size();
		assertEquals("Should have added one contact", sizeBefore+1, sizeAfter);
		final int incomingSizeAfter = contactAccount.getIncomingContacts().size();
		assertEquals("Should have added one incoming contact", incomingSizeBefore+1, incomingSizeAfter);
	}

	@Test
	public void createContactRequest_alreadyContact() {
		final int sizeBefore = account.getContacts().size();
		final int incomingSizeBefore = contactAccount.getIncomingContacts().size();
		instance.createContactRequest(CONTACT_ACCOUNT_ID);
		verify(contactRepository, never()).save(any(ContactEntity.class));
		final int sizeAfter = account.getContacts().size();
		assertEquals("Should not have added any contact", sizeBefore, sizeAfter);
		final int incomingSizeAfter = contactAccount.getIncomingContacts().size();
		assertEquals("Should not have added any incoming contact", incomingSizeBefore, incomingSizeAfter);
	}

	@Test
	public void createContactRequest_alreadyIncomingRequest() {
		account.getContacts().remove(contact);
		account.getIncomingContacts().add(contact);
		when(contact.getOwner()).thenReturn(contactAccount);
		when(contact.getContact()).thenReturn(account);
		when(contact.getContactType()).thenReturn(ContactType.REQUESTED);
		final int sizeBefore = contactAccount.getContacts().size();
		final int incomingSizeBefore = account.getIncomingContacts().size();
		instance.createContactRequest(CONTACT_ACCOUNT_ID);
		verify(contactRepository, never()).save(any(ContactEntity.class));
		final int sizeAfter = contactAccount.getContacts().size();
		assertEquals("Should not have added any contact", sizeBefore, sizeAfter);
		final int incomingSizeAfter = account.getIncomingContacts().size();
		assertEquals("Should not have added any incoming contact", incomingSizeBefore, incomingSizeAfter);
	}

	@Test
	public void createContactRequest_ownAccount() {
		final int sizeBefore = account.getContacts().size();
		final int incomingSizeBefore = account.getIncomingContacts().size();
		instance.createContactRequest(ACCOUNT_ID);
		verify(contactRepository, never()).save(any(ContactEntity.class));
		final int sizeAfter = account.getContacts().size();
		assertEquals("Should not have added any contact", sizeBefore, sizeAfter);
		final int incomingSizeAfter = account.getIncomingContacts().size();
		assertEquals("Should not have added any incoming contact", incomingSizeBefore, incomingSizeAfter);
	}

	@Test
	public void deleteContact_ok() {
		final int sizeBefore = account.getContacts().size();
		final int incomingSizeBefore = contactAccount.getIncomingContacts().size();
		instance.deleteContact(CONTACT_ID);
		verify(contactRepository).delete(contact);
		final int sizeAfter = account.getContacts().size();
		assertEquals("Should have removed one contact", sizeBefore-1, sizeAfter);
		final int incomingSizeAfter = contactAccount.getIncomingContacts().size();
		assertEquals("Should have removed one incoming contact", incomingSizeBefore-1, incomingSizeAfter);
	}

	@Test
	public void deleteContact_contactNotFound() {
		final int sizeBefore = account.getContacts().size();
		instance.deleteContact(9999L);
		verify(contactRepository, never()).delete(any(ContactEntity.class));
		final int sizeAfter = account.getContacts().size();
		assertEquals("Should not have removed any contact", sizeBefore, sizeAfter);
	}

	private void assertContactAccountIDs(final List<ContactReferenceOutput> contacts, final Long... accountIDs) {
		final List<Long> expected = Arrays.asList(accountIDs);
		final List<Long> actual = new ArrayList<>();
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
		when(contactAccount.getId()).thenReturn(CONTACT_ACCOUNT_ID);
		when(anotherAccount.getId()).thenReturn(ANOTHER_ACCOUNT_ID);
		when(anotherAccount.getAccountType()).thenReturn(ENTITY_ACCOUNT_TYPE);
		// Set up a contact
		when(contact.getId()).thenReturn(CONTACT_ID);
		when(contact.getOwner()).thenReturn(account);
		when(contact.getContact()).thenReturn(contactAccount);
		when(contact.getContactType()).thenReturn(ContactType.APPROVED);
		// Set up another contact
		when(anotherContact.getId()).thenReturn(ANOTHER_CONTACT_ID);
		when(anotherContact.getOwner()).thenReturn(account);
		when(anotherContact.getContact()).thenReturn(anotherAccount);
		when(anotherContact.getContactType()).thenReturn(ContactType.REQUESTED);
		// Set up contact collections for account
		final Set<ContactEntity> contacts = new HashSet<>();
		contacts.add(contact);
		contacts.add(anotherContact);
		when(account.getContacts()).thenReturn(contacts);
		final Set<ContactEntity> incomingContacts = new HashSet<>();
		when(account.getIncomingContacts()).thenReturn(incomingContacts);
		// Set up incoming contact collection for contact account
		final Set<ContactEntity> contactIncomingContacts = new HashSet<>();
		contactIncomingContacts.add(contact);
		when(contactAccount.getIncomingContacts()).thenReturn(contactIncomingContacts);
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(account);
		when(accessControlService.encodePassword(FORM_PASSWORD)).thenReturn(ENCODED_PASSWORD);
	}

	@Before
	public void setupAccountRepository() {
		when(accountRepository.findById(ACCOUNT_ID)).thenReturn(account);
		when(accountRepository.findById(ANOTHER_ACCOUNT_ID)).thenReturn(anotherAccount);
		when(accountRepository.findById(CONTACT_ACCOUNT_ID)).thenReturn(contactAccount);
		allAccounts = new ArrayList<>();
		allAccounts.add(account);
		allAccounts.add(anotherAccount);
		allAccounts.add(contactAccount);
		when(accountRepository.findAll(any(Sort.class))).thenReturn(allAccounts);
	}

	@Before
	public void setupInstance() {
		instance = new AccountService();
		instance.accountRepository = accountRepository;
		instance.contactRepository = contactRepository;
		instance.accessControlService = accessControlService;
	}
}
