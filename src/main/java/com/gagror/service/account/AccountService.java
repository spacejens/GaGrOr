package com.gagror.service.account;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;

@Service
@Transactional
@CommonsLog
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

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
		// TODO Fail if password does not match in form
		// Find the account to update
		final AccountEntity entity = accountRepository.findById(editAccountForm.getId());
		if(null == entity) {
			throw new IllegalStateException(String.format("Failed to find edited account ID %d when saving", editAccountForm.getId()));
		}
		if(! editAccountForm.getVersion().equals(entity.getVersion())) {
			throw new IllegalStateException(String.format("Simultaneous edit of account ID %d detected when saving", editAccountForm.getId()));
		}
		entity.setUsername(editAccountForm.getUsername());
		// TODO Support editing account type (but not for yourself?)
		// TODO Support password change (requires changing of security authentication when editing for current user)
	}
}
