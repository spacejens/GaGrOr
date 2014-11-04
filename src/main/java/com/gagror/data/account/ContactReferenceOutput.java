package com.gagror.data.account;

import lombok.Getter;

public class ContactReferenceOutput extends AccountReferenceOutput {

	@Getter
	private final ContactType contactType;

	@Getter
	private final Long contactId;

	@Getter
	private final boolean incoming;

	public ContactReferenceOutput(final ContactEntity entity, final boolean showOwner) {
		super(showOwner ? entity.getOwner() : entity.getContact());
		contactType = entity.getContactType();
		contactId = entity.getId();
		incoming = showOwner;
	}

	public ContactReferenceOutput(final AccountEntity account) {
		super(account);
		contactType = null;
		contactId = null;
		incoming = false;
	}
}
