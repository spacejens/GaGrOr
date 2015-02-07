package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import com.gagror.data.Identifiable;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupOwned;
import com.gagror.data.group.PlayerOwned;

@CommonsLog
public abstract class AbstractPermissionPlayer<E extends Identifiable<Long> & PlayerOwned & GroupOwned>
extends AbstractGagrorPermissionLongId<E> {

	protected AbstractPermissionPlayer(final String name, final Class<E> targetClass) {
		super(name, targetClass);
	}

	@Override
	public final boolean hasPermission(final Long id, final AccountEntity account) {
		final E target = loadTarget(id);
		// Allow if player
		if(target.getPlayer().equals(account)) {
			log.debug(String.format("Account %s is player of %s", account, target));
			return true;
		}
		// TODO Group owners should be granted player permission for all targets within the group
		// Disallow for everyone else
		return false;
	}

	protected abstract E loadTarget(final Long id);
}
