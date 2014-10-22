package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ContactReferenceOutput extends AccountReferenceOutput {

	private final ContactType contactType;

	public ContactReferenceOutput(final ContactEntity entity) {
		super(entity.getContact());
		contactType = entity.getContactType();
	}
}
