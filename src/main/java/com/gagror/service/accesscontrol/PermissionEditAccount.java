package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;

@CommonsLog
@Component
public class PermissionEditAccount extends AbstractGagrorPermission<Long, AccountEntity> {

	@Autowired
	AccountRepository accountRepository;

	public PermissionEditAccount() {
		super("editAccount", AccountEntity.class);
	}

	@Override
	protected Long parseId(final String rawId) {
		return Long.parseLong(rawId);
	}

	@Override
	protected boolean hasPermission(final Long id, final AccountEntity account) {
		final AccountEntity editedAccount = accountRepository.findById(id);
		if(id.equals(account.getId())) {
			log.debug(String.format("Permitted to edit own account %d", id));
			return true;
		} else if(account.getAccountType().getMayEdit().contains(editedAccount.getAccountType())) {
			log.debug(String.format("Administrator %d permitted to edit account %d", account.getId(), id));
			return true;
		} else {
			log.warn(String.format("Account %d not permitted to edit account %d", account.getId(), id));
			return false;
		}
	}
}
