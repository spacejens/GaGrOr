package com.gagror.service.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import com.gagror.data.account.ContactViewOutput;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
@Transactional
@CommonsLog
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

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
		final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
		filterAccounts: for(final AccountEntity account : accountRepository.findAll(new Sort("username"))) {
			// Filter out contacts
			for(final ContactEntity contact : requestAccount.getContacts()) {
				if(contact.getContact().equals(account)) {
					continue filterAccounts;
				}
			}
			// Filter out incoming contact requests
			for(final ContactEntity incoming : requestAccount.getIncomingContacts()) {
				if(incoming.getOwner().equals(account)) {
					continue filterAccounts;
				}
			}
			// Filter out the user's own account
			if(requestAccount.equals(account)) {
				continue filterAccounts;
			}
			// Passed filtering, add this account
			output.add(new ContactReferenceOutput(account));
		}
		return output;
	}

	public ContactViewOutput loadContact(final Long contactId) {
		final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
		for(final ContactEntity contact : requestAccount.getContacts()) {
			if(contact.getId().equals(contactId)) {
				return new ContactViewOutput(contact);
			}
		}
		log.error(String.format("Failed to find contact %d for account %d", contactId, requestAccount.getId()));
		return null;
	}
}
