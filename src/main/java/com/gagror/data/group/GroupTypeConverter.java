package com.gagror.data.group;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.gagror.data.AttributeConverterSupport;

@Converter(autoApply = true)
public class GroupTypeConverter
extends AttributeConverterSupport<Integer, GroupType>
implements AttributeConverter<GroupType, Integer> {

	@Override
	public GroupType convertToEntityAttribute(final Integer dbData) {
		return GroupType.fromId(dbData);
	}
}
