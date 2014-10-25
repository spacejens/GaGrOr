package com.gagror.service.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.thymeleaf.util.StringUtils;

import com.gagror.data.account.AccountEditInput;
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
		final AccountEntity entity = accountRepository.findById(accountId);
		if(null != entity) {
			log.debug(String.format("Loaded account ID %d (%s) for logging in", accountId, entity.getUsername()));
			accessControlService.logInAs(entity);
		} else {
			log.error(String.format("Failed to load account ID %d for editing", accountId));
		}
	}

	public AccountEditOutput loadAccountForEditing(final Long accountId) {
		final AccountEntity entity = accountRepository.findById(accountId);
		if(null != entity) {
			log.debug(String.format("Loaded account ID %d (%s) for editing", accountId, entity.getUsername()));
			return new AccountEditOutput(entity);
		} else {
			log.warn(String.format("Failed to load account ID %d for editing", accountId));
			return null;
		}
	}

	public void saveAccount(final AccountEditInput editAccountForm, final BindingResult bindingResult) {
		// Find the account to update
		final AccountEntity entity = accountRepository.findById(editAccountForm.getId());
		if(null == entity) {
			throw new IllegalStateException(String.format("Failed to find edited account ID %d when saving", editAccountForm.getId()));
		}
		final boolean editingOwnAccount = entity.getId().equals(accessControlService.getRequestAccountEntity().getId());
		// Validate input before updating the entity
		if(! StringUtils.isEmptyOrWhitespace(editAccountForm.getPassword())
				|| ! StringUtils.isEmptyOrWhitespace(editAccountForm.getPasswordRepeat())) {
			// Only validate password input if there was any input
			if(! editAccountForm.getPassword().equals(editAccountForm.getPasswordRepeat())) {
				editAccountForm.addErrorPasswordMismatch(bindingResult);
			}
			if(accessControlService.isPasswordTooWeak(editAccountForm.getPassword())) {
				editAccountForm.addErrorPasswordTooWeak(bindingResult);
			}
		}
		if(editingOwnAccount
				&& ! entity.getUsername().equals(editAccountForm.getUsername())
				&& null != accountRepository.findByUsername(editAccountForm.getUsername())) {
			editAccountForm.addErrorUsernameBusy(bindingResult);
		}
		if(! editingOwnAccount
				&& ! accessControlService.getRequestAccountEntity().getAccountType().getMayEdit().contains(editAccountForm.getAccountType())) {
			editAccountForm.addErrorDisallowedAccountType(bindingResult);
		}
		if(! editAccountForm.getVersion().equals(entity.getVersion())) {
			editAccountForm.addErrorSimultaneuosEdit(bindingResult);
		}
		// Stop if errors have been detected
		if(bindingResult.hasErrors()) {
			return;
		}
		// Everything is OK, update the entity
		if(editingOwnAccount) {
			/*
			 * Editing username for other accounts is forbidden, since it would be very confusing.
			 * That user would be thrown out and unable to log in again.
			 */
			entity.setUsername(editAccountForm.getUsername());
		}
		if(! StringUtils.isEmptyOrWhitespace(editAccountForm.getPassword())) {
			entity.setPassword(accessControlService.encodePassword(editAccountForm.getPassword()));
		}
		if(! editingOwnAccount) {
			// Cannot edit account type or flags of one's own account
			entity.setAccountType(editAccountForm.getAccountType());
			entity.setActive(editAccountForm.isActive());
			entity.setLocked(editAccountForm.isLocked());
		}
		// If the currently logged in user was edited, make sure that the user is still logged in
		if(editingOwnAccount) {
			accessControlService.logInAs(entity);
		}
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
		for(final AccountEntity account : accountRepository.findAll(new Sort("username"))) {
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
				log.trace(String.format("Account %d (%s) is contact account", account.getId(), account.getUsername()));
				return false;
			}
		}
		// Filter out incoming contact requests
		for(final ContactEntity incoming : requestAccount.getIncomingContacts()) {
			if(incoming.getOwner().equals(account)) {
				log.trace(String.format("Account %d (%s) is incoming contact account", account.getId(), account.getUsername()));
				return false;
			}
		}
		// Filter out the user's own account
		if(requestAccount.equals(account)) {
			log.trace(String.format("Account %d (%s) is own account", account.getId(), account.getUsername()));
			return false;
		}
		// Account was not filtered out
		log.trace(String.format("Account %d (%s) is non contact account", account.getId(), account.getUsername()));
		return true;
	}

	private ContactEntity findContact(final Set<ContactEntity> contacts, final Long contactId) {
		for(final ContactEntity contact : contacts) {
			if(contact.getId().equals(contactId)) {
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
		final AccountEntity account = accountRepository.findById(accountId);
		if(isNonContactAccount(account)) {
			final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
			final ContactEntity contact = new ContactEntity(requestAccount, ContactType.REQUESTED, account);
			contactRepository.save(contact);
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
			final ContactEntity mirroredContact = new ContactEntity(contact.getContact(), ContactType.APPROVED, contact.getOwner());
			contactRepository.save(mirroredContact);
			return;
		}
		log.error(String.format("Failed to find incoming contact request %d", contactId));
	}
}
