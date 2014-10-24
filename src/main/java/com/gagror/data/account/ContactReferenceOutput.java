package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ContactReferenceOutput extends AccountReferenceOutput {

	private final ContactType contactType;

	private final Long contactId;

	public ContactReferenceOutput(final ContactEntity entity, final boolean showOwner) {
		super(showOwner ? entity.getOwner() : entity.getContact());
		contactType = entity.getContactType();
		contactId = entity.getId();
	}

	public ContactReferenceOutput(final AccountEntity account) {
		super(account);
		contactType = null;
		contactId = null;
	}
}
