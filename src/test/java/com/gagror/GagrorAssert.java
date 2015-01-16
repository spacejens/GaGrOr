package com.gagror;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gagror.data.Identifiable;
import com.gagror.data.Named;

public final class GagrorAssert {

	private GagrorAssert() {
		throw new UnsupportedOperationException("All methods are static, instances should not be created");
	}

	public static <T extends Identifiable<Long>> void assertIds(
			final List<T> identifiable,
			final Long... expectedIds) {
		// This method is not using generic type for varargs parameter because of heap pollution warning
		final List<Long> expected = Arrays.asList(expectedIds);
		final List<Long> actual = new ArrayList<>(identifiable.size());
		for(final T n : identifiable) {
			actual.add(n.getId());
		}
		assertEquals("Wrong IDs", expected, actual);
	}

	public static <N extends Named> void assertNames(
			final List<N> named,
			final String... expectedNames) {
		final List<String> expected = Arrays.asList(expectedNames);
		final List<String> actual = new ArrayList<>(named.size());
		for(final N n : named) {
			actual.add(n.getName());
		}
		assertEquals("Wrong names", expected, actual);
	}

	public static void assertFieldHasGetter(final Field field, final Class<?> clazz) {
		final String getterName = calculateGetterSetterName(field, false);
		try {
			final Method getter = clazz.getMethod(getterName);
			assertEquals("Wrong return type of getter", field.getType(), getter.getReturnType());
		} catch (Exception e) {
			fail(String.format("Failed to find getter for field %s in class %s", field.getName(), clazz.getSimpleName()));
		}
	}

	public static void assertFieldHasSetter(final Field field, final Class<?> clazz) {
		final String getterName = calculateGetterSetterName(field, true);
		try {
			final Method setter = clazz.getMethod(getterName, field.getType());
			assertEquals("Wrong return type of setter", void.class, setter.getReturnType());
		} catch (Exception e) {
			fail(String.format("Failed to find setter for field %s in class %s", field.getName(), clazz.getSimpleName()));
		}
	}

	private static String calculateGetterSetterName(final Field field, final boolean setter) {
		final StringBuffer methodName = new StringBuffer();
		if(setter) {
			methodName.append("set");
		} else if(field.getType().equals(boolean.class)) {
			methodName.append("is");
		} else {
			methodName.append("get");
		}
		methodName.append(String.valueOf(field.getName().charAt(0)).toUpperCase());
		methodName.append(field.getName().substring(1));
		return methodName.toString();
	}
}
