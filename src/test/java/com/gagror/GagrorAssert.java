package com.gagror;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gagror.data.Identifiable;
import com.gagror.data.Named;

public class GagrorAssert {

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
}
