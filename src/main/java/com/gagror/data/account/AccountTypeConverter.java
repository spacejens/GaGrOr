package com.gagror.data.account;

import javax.persistence.Converter;

import com.gagror.data.AttributeConverterSupport;

@Converter(autoApply = true)
public class AccountTypeConverter extends AttributeConverterSupport<Integer, AccountType> {

	@Override
	public AccountType convertToEntityAttribute(final Integer dbData) {
		return AccountType.fromId(dbData);
	}
}
