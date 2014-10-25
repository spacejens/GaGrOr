package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Component;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.ContactEntity;

@CommonsLog
@Component
public class PermissionHasContactRequest extends AbstractGagrorPermissionLongId<ContactEntity> {

	public PermissionHasContactRequest() {
		super("hasContactRequest", ContactEntity.class);
	}

	@Override
	protected boolean hasPermission(final Long id, final AccountEntity account) {
		for(final ContactEntity contact : account.getIncomingContacts()) {
			if(contact.getId().equals(id) && contact.getContactType().isRequest()) {
				log.debug(String.format("Account %d has incoming contact request %d", account.getId(), id));
				return true;
			}
		}
		log.debug(String.format("Account %d doesn't have incoming contact request %d", account.getId(), id));
		return false;
	}
}
