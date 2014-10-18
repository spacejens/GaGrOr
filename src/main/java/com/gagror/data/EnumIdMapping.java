package com.gagror.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnumIdMapping<I extends Serializable, E extends Enum<E> & Identifiable<I>> {

	private final Map<I,E> map;

	public EnumIdMapping(final Class<E> clazz) {
		final Map<I,E> tempMap = new HashMap<>();
		for(final E instance : clazz.getEnumConstants()) {
			if(tempMap.containsKey(instance.getId())) {
				throw new IllegalArgumentException(String.format("Enum %s has duplicate ID %s", clazz.getCanonicalName(), instance.getId()));
			}
			tempMap.put(instance.getId(), instance);
		}
		map = Collections.unmodifiableMap(tempMap);
	}

	public E fromId(final I id) {
		if(! map.containsKey(id)) {
			throw new IllegalArgumentException(String.format("Unknown enum ID %s", id));
		}
		return map.get(id);
	}
}
