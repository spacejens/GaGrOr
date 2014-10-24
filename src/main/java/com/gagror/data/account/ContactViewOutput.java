package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ContactViewOutput extends ContactReferenceOutput {

	public ContactViewOutput(final ContactEntity entity) {
		super(entity, false);
	}
}
