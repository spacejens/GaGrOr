package com.gagror.data;

import static org.junit.Assert.assertEquals;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.junit.Before;
import org.junit.Test;

import com.gagror.CodingErrorException;

public class EnumIdMappingUnitTest {

	EnumIdMapping<Integer, TestEnum> instance;

	@Test
	public void fromId_ok() {
		for(final TestEnum val : TestEnum.values()) {
			assertEquals(String.format("Unexpected result when finding %s in mapping", val), val, instance.fromId(val.getId()));
		}
	}

	@Test(expected=CodingErrorException.class)
	public void fromId_unused() {
		instance.fromId(99);
	}

	@Test(expected=CodingErrorException.class)
	public void constructor_disallowDuplicateIDs() {
		new EnumIdMapping<>(TestEnumWithDuplicateId.class);
	}

	@Before
	public void createInstance() {
		instance = new EnumIdMapping<>(TestEnum.class);
	}

	@RequiredArgsConstructor
	private enum TestEnum implements Identifiable<Integer> {

		FIRST(1),
		SECOND(2);

		@Getter
		private final Integer id;
	}

	@RequiredArgsConstructor
	private enum TestEnumWithDuplicateId implements Identifiable<Integer> {

		ALPHA(33),
		BETA(33);

		@Getter
		private final Integer id;
	}
}
