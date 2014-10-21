package com.gagror.data.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ContactTypeConverter implements AttributeConverter<ContactType, Integer> {

	// TODO Create abstract superclass to share logic with AccountTypeConverter
	@Override
	public Integer convertToDatabaseColumn(final ContactType attribute) {
		return attribute.getId();
	}

	@Override
	public ContactType convertToEntityAttribute(final Integer dbData) {
		return ContactType.fromId(dbData);
	}
}
