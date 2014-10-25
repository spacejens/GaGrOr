package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Component;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.ContactEntity;

@CommonsLog
@Component
public class PermissionHasContact extends AbstractGagrorPermissionLongId<ContactEntity> {

	public PermissionHasContact() {
		super("hasContact", ContactEntity.class);
	}

	@Override
	protected boolean hasPermission(final Long id, final AccountEntity account) {
		for(final ContactEntity contact : account.getContacts()) {
			if(contact.getId().equals(id)) {
				log.debug(String.format("Account %d has contact %d", account.getId(), id));
				return true;
			}
		}
		log.debug(String.format("Account %d doesn't have contact %d", account.getId(), id));
		return false;
	}
}
