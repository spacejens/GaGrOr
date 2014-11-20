package com.gagror;

import java.util.HashSet;
import java.util.Set;

public abstract class EnumUnitTestSupport<E extends Enum<E>> {

	protected static <E extends Enum<E>> Iterable<Object[]> parameterizeForInstances(final Class<E> clazz) {
		final Set<Object[]> parameters = new HashSet<>();
		for(final E instance : clazz.getEnumConstants()) {
			parameters.add(new Object[]{instance});
		}
		return parameters;
	}

	// TODO Add enum design rules test to verify fromId method exists, and that property names exist in enums.properties
}
