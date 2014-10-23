package com.gagror.data;

import java.io.Serializable;

import javax.persistence.AttributeConverter;

public abstract class AttributeConverterSupport<I extends Serializable, E extends Enum<E> & Identifiable<I>>
implements AttributeConverter<E, I> {

	@Override
	public final I convertToDatabaseColumn(final E attribute) {
		return attribute.getId();
	}
}
