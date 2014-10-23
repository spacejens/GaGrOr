package com.gagror.data.account;

import javax.persistence.Converter;

import com.gagror.data.AttributeConverterSupport;

@Converter(autoApply = true)
public class ContactTypeConverter extends AttributeConverterSupport<Integer, ContactType> {

	@Override
	public ContactType convertToEntityAttribute(final Integer dbData) {
		return ContactType.fromId(dbData);
	}
}
