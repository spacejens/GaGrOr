package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountType;

@CommonsLog
public class PermissionEditAccount extends AbstractGagrorPermission<Long, AccountEntity> {

	public PermissionEditAccount() {
		super("editAccount", AccountEntity.class);
	}

	@Override
	protected Long parseId(final String rawId) {
		return Long.parseLong(rawId);
	}

	@Override
	protected boolean hasPermission(final Long id, final AccountEntity account) {
		if(id.equals(account.getId())) {
			log.debug(String.format("Permitted to edit own account %d", id));
			return true;
		} else if(AccountType.SYSTEM_OWNER.equals(account.getAccountType())) {
			// TODO System owners not allowed to edit each other's accounts? Delegate decision to account type?
			log.debug(String.format("Administrator %d permitted to edit account %d", account.getId(), id));
			return true;
		} else {
			log.warn(String.format("Account %d not permitted to edit account %d", account.getId(), id));
			return false;
		}
	}
}
