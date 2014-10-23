package com.gagror.data.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.gagror.data.AttributeConverterSupport;

@Converter(autoApply = true)
public class AccountTypeConverter
extends AttributeConverterSupport<Integer, AccountType>
implements AttributeConverter<AccountType, Integer> {

	@Override
	public AccountType convertToEntityAttribute(final Integer dbData) {
		return AccountType.fromId(dbData);
	}
}
