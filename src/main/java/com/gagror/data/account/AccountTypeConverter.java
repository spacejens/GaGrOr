package com.gagror.data.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(final AccountType attribute) {
		return attribute.getId();
	}

	@Override
	public AccountType convertToEntityAttribute(final Integer dbData) {
		return AccountType.fromId(dbData);
	}
}
