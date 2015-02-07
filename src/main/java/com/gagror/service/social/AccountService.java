package com.gagror.service.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.account.AccountEditOutput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.account.ContactReferenceOutput;
import com.gagror.data.account.ContactRepository;
import com.gagror.data.account.ContactType;
import com.gagror.data.account.ContactViewOutput;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
@Transactional
@CommonsLog
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	AccessControlService accessControlService;

	public void loginAsUser(final Long accountId) {
		final AccountEntity entity = accountRepository.load(accountId);
		log.debug(String.format("Loaded account ID %d (%s) for logging in", accountId, entity.getName()));
		accessControlService.logInAs(entity);
	}

	public AccountEditOutput loadAccountForEditing(final Long accountId) {
		final AccountEntity entity = accountRepository.load(accountId);
		log.debug(String.format("Loaded account ID %d (%s) for editing", accountId, entity.getName()));
		return new AccountEditOutput(entity);
	}

	public List<ContactReferenceOutput> loadContacts() {
		log.debug("Loading contacts");
		final List<ContactReferenceOutput> output = new ArrayList<>();
		for(final ContactEntity entity : accessControlService.getRequestAccountEntity().getContacts()) {
			if(entity.getContactType().isContact()) {
				output.add(new ContactReferenceOutput(entity, false));
			}
		}
		Collections.sort(output);
		return output;
	}

	public List<ContactReferenceOutput> loadSentContactRequests() {
		log.debug("Loading sent contact requests");
		final List<ContactReferenceOutput> output = new ArrayList<>();
		for(final ContactEntity entity : accessControlService.getRequestAccountEntity().getContacts()) {
			if(entity.getContactType().isRequest()) {
				output.add(new ContactReferenceOutput(entity, false));
			}
		}
		Collections.sort(output);
		return output;
	}

	public List<ContactReferenceOutput> loadReceivedContactRequests() {
		log.debug("Loading received contact requests");
		final List<ContactReferenceOutput> output = new ArrayList<>();
		for(final ContactEntity entity : accessControlService.getRequestAccountEntity().getIncomingContacts()) {
			if(entity.getContactType().isRequest()) {
				output.add(new ContactReferenceOutput(entity, true));
			}
		}
		Collections.sort(output);
		return output;
	}

	public List<ContactReferenceOutput> loadAccountsNotContacts() {
		log.debug("Loading non-contact accounts as contacts");
		final List<ContactReferenceOutput> output = new ArrayList<>();
		for(final AccountEntity account : accountRepository.list()) {
			if(isNonContactAccount(account)) {
				output.add(new ContactReferenceOutput(account));
			}
		}
		return output;
	}

	private boolean isNonContactAccount(final AccountEntity account) {
		final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
		// Filter out contacts
		for(final ContactEntity contact : requestAccount.getContacts()) {
			if(contact.getContact().equals(account)) {
				log.trace(String.format("Account %d (%s) is contact account", account.getId(), account.getName()));
				return false;
			}
		}
		// Filter out incoming contact requests
		for(final ContactEntity incoming : requestAccount.getIncomingContacts()) {
			if(incoming.getOwner().equals(account)) {
				log.trace(String.format("Account %d (%s) is incoming contact account", account.getId(), account.getName()));
				return false;
			}
		}
		// Filter out the user's own account
		if(requestAccount.equals(account)) {
			log.trace(String.format("Account %d (%s) is own account", account.getId(), account.getName()));
			return false;
		}
		// Account was not filtered out
		log.trace(String.format("Account %d (%s) is non contact account", account.getId(), account.getName()));
		return true;
	}

	private ContactEntity findContact(final Set<ContactEntity> contacts, final Long contactId) {
		for(final ContactEntity contact : contacts) {
			if(contact.hasId(contactId)) {
				return contact;
			}
		}
		return null;
	}

	public ContactViewOutput loadContact(final Long contactId) {
		final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
		final ContactEntity contact = findContact(requestAccount.getContacts(), contactId);
		if(null != contact) {
			return new ContactViewOutput(contact);
		}
		log.error(String.format("Failed to find contact %d for account %d", contactId, requestAccount.getId()));
		return null;
	}

	public void createContactRequest(final Long accountId) {
		final AccountEntity account = accountRepository.load(accountId);
		if(isNonContactAccount(account)) {
			final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
			createContact(requestAccount, ContactType.REQUESTED, account);
		}
	}

	public void deleteContact(final Long contactId) {
		deleteContact(contactId, accessControlService.getRequestAccountEntity().getContacts());
	}

	private void deleteContact(final Long contactId, final Set<ContactEntity> contacts) {
		final ContactEntity contact = findContact(contacts, contactId);
		if(null != contact) {
			// Also delete the mirrored contact if it exists
			for(final ContactEntity mirroredContact : contact.getOwner().getIncomingContacts()) {
				if(mirroredContact.getOwner().equals(contact.getContact())) {
					log.debug(String.format("Deleting mirrored contact %d for account %d, owned by %d", mirroredContact.getId(), mirroredContact.getContact().getId(), mirroredContact.getOwner().getId()));
					mirroredContact.getOwner().getContacts().remove(mirroredContact);
					mirroredContact.getContact().getIncomingContacts().remove(mirroredContact);
					contactRepository.delete(mirroredContact);
				}
			}
			log.debug(String.format("Deleting contact %d for account %d, owned by %d", contactId, contact.getContact().getId(), contact.getOwner().getId()));
			contact.getOwner().getContacts().remove(contact);
			contact.getContact().getIncomingContacts().remove(contact);
			contactRepository.delete(contact);
			return;
		}
	}

	public void declineContactRequest(final Long contactId) {
		deleteContact(contactId, accessControlService.getRequestAccountEntity().getIncomingContacts());
	}

	public void acceptContactRequest(final Long contactId) {
		final ContactEntity contact = findContact(accessControlService.getRequestAccountEntity().getIncomingContacts(), contactId);
		if(null != contact && contact.getContactType().isRequest()) {
			log.debug(String.format("Accepting contact request %d from account %d to %d", contactId, contact.getOwner().getId(), contact.getContact().getId()));
			contact.setContactType(ContactType.APPROVED);
			mirrorContact(contact);
			return;
		}
		log.error(String.format("Failed to find incoming contact request %d", contactId));
	}

	public ContactEntity createContact(final AccountEntity owner, final ContactType contactType, final AccountEntity account) {
		final ContactEntity contact = new ContactEntity(owner, contactType, account);
		return contactRepository.persist(contact);
	}

	public ContactEntity mirrorContact(final ContactEntity original) {
		return createContact(original.getContact(), original.getContactType(), original.getOwner());
	}
}
