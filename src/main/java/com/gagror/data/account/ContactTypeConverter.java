package com.gagror.data.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.gagror.data.AttributeConverterSupport;

@Converter(autoApply = true)
public class ContactTypeConverter
extends AttributeConverterSupport<Integer, ContactType>
implements AttributeConverter<ContactType, Integer> {

	@Override
	public ContactType convertToEntityAttribute(final Integer dbData) {
		return ContactType.fromId(dbData);
	}
}
