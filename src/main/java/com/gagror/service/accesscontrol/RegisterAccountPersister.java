package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.RegisterInput;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class RegisterAccountPersister extends AbstractPersister<RegisterInput, AccountEntity> {

	@Autowired
	AccessControlService accessControlService;

	@Autowired
	AccountRepository accountRepository;

	@Override
	protected void validateForm(final RegisterInput form, final BindingResult bindingResult) {
		if(null != accountRepository.findByUsername(form.getUsername())) {
			log.warn(String.format("Attempt to create user '%s' failed, username busy", form.getUsername()));
			form.addErrorUsernameBusy(bindingResult);
		}
		if(accessControlService.isPasswordTooWeak(form.getPassword())) {
			log.warn(String.format("Attempt to create user '%s' failed, password too weak", form.getUsername()));
			form.addErrorPasswordTooWeak(bindingResult);
		}
		if(! form.getPassword().equals(form.getPasswordRepeat())) {
			log.warn(String.format("Attempt to create user '%s' failed, password repeat mismatch", form.getUsername()));
			form.addErrorPasswordMismatch(bindingResult);
		}
	}

	@Override
	protected boolean isCreateNew(final RegisterInput form) {
		return true;
	}

	@Override
	protected AccountEntity createNew(final RegisterInput form) {
		return new AccountEntity(
				form.getUsername(),
				accessControlService.encodePassword(form.getPassword()));
	}

	@Override
	protected void updateValues(final RegisterInput form, final AccountEntity entity) {
		// Nothing to do, all data is set when creating the entity
	}

	@Override
	protected AccountEntity makePersistent(final AccountEntity entity) {
		return accountRepository.save(entity);
	}

	@Override
	protected void postPersistenceUpdate(final AccountEntity entity) {
		accessControlService.logInAs(entity);
	}
}
