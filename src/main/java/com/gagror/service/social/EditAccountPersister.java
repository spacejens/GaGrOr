package com.gagror.service.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.thymeleaf.util.StringUtils;

import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.service.AbstractPersister;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
public class EditAccountPersister extends AbstractPersister<AccountEditInput, AccountEntity> {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccessControlService accessControlService;

	@Override
	protected void validateForm(final AccountEditInput form, final BindingResult bindingResult) {
		final boolean editingOwnAccount = isEditingOwnAccount(form.getId());
		// Validate input before updating the entity
		if(! StringUtils.isEmptyOrWhitespace(form.getPassword())
				|| ! StringUtils.isEmptyOrWhitespace(form.getPasswordRepeat())) {
			// Only validate password input if there was any input
			if(! form.getPassword().equals(form.getPasswordRepeat())) {
				form.addErrorPasswordMismatch(bindingResult);
			}
			if(accessControlService.isPasswordTooWeak(form.getPassword())) {
				form.addErrorPasswordTooWeak(bindingResult);
			}
		}
		if(! editingOwnAccount
				&& ! accessControlService.getRequestAccountEntity().getAccountType().getMayEdit().contains(form.getAccountType())) {
			form.addErrorDisallowedAccountType(bindingResult);
		}
	}

	@Override
	protected void validateFormVsExistingState(final AccountEditInput form, final BindingResult bindingResult, final AccountEntity entity) {
		final boolean editingOwnAccount = isEditingOwnAccount(form.getId());
		if(editingOwnAccount
				&& ! entity.getUsername().equals(form.getUsername())
				&& null != accountRepository.findByUsername(form.getUsername())) {
			form.addErrorUsernameBusy(bindingResult);
		}
		if(! form.getVersion().equals(entity.getVersion())) {
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected AccountEntity createOrLoad(final AccountEditInput form) {
		final AccountEntity entity = accountRepository.findById(form.getId());
		if(null == entity) {
			throw new IllegalStateException(String.format("Failed to find edited account ID %d when saving", form.getId()));
		}
		return entity;
	}

	@Override
	protected void updateValues(final AccountEditInput form, final AccountEntity entity) {
		final boolean editingOwnAccount = isEditingOwnAccount(entity.getId());
		if(editingOwnAccount) {
			/*
			 * Editing username for other accounts is forbidden, since it would be very confusing.
			 * That user would be thrown out and unable to log in again.
			 */
			entity.setUsername(form.getUsername());
		}
		if(! StringUtils.isEmptyOrWhitespace(form.getPassword())) {
			entity.setPassword(accessControlService.encodePassword(form.getPassword()));
		}
		if(! editingOwnAccount) {
			// Cannot edit account type or flags of one's own account
			entity.setAccountType(form.getAccountType());
			entity.setActive(form.isActive());
			entity.setLocked(form.isLocked());
		}
	}

	@Override
	protected void updateApplicationState(final AccountEntity entity) {
		if(isEditingOwnAccount(entity.getId())) {
			accessControlService.logInAs(entity);
		}
	}

	private boolean isEditingOwnAccount(final Long id) {
		return id.equals(accessControlService.getRequestAccountEntity().getId());
	}
}
