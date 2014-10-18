package com.gagror.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeConverter;

import lombok.RequiredArgsConstructor;

import org.junit.Test;

@RequiredArgsConstructor
public abstract class AttributeConverterUnitTestSupport<I extends Serializable, E extends Enum<E> & Identifiable<I>, C extends AttributeConverter<E, I>> {

	private final E attribute;

	private final C instance;

	@Test
	public void convertToDatabaseColumn() {
		assertEquals(attribute.getId(), instance.convertToDatabaseColumn(attribute));
	}

	@Test
	public void convertToEntityAttribute() {
		assertSame(attribute, instance.convertToEntityAttribute(attribute.getId()));
	}

	protected static <E extends Enum<E>> Collection<Object[]> getParameters(final Class<E> clazz) {
		final ArrayList<Object[]> output = new ArrayList<>();
		for(final E attribute : clazz.getEnumConstants()) {
			output.add(new Object[]{attribute});
		}
		return output;
	}
}
