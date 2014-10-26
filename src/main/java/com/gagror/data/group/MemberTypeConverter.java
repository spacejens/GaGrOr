package com.gagror.data.group;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.gagror.data.AttributeConverterSupport;

@Converter(autoApply = true)
public class MemberTypeConverter
extends AttributeConverterSupport<Integer, MemberType>
implements AttributeConverter<MemberType, Integer> {

	@Override
	public MemberType convertToEntityAttribute(final Integer dbData) {
		return MemberType.fromId(dbData);
	}
}
