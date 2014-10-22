package com.gagror.service.account;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.gagror.service.accesscontrol.AccessControlService;

@Service
@Transactional
@CommonsLog
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccessControlService accessControlService;

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
			output.add(new ContactReferenceOutput(entity));
		}
		return output;
	}
}
