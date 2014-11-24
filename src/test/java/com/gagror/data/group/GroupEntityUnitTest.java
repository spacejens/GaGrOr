package com.gagror.data.group;

import static org.junit.Assert.fail;

import org.junit.Test;

public class GroupEntityUnitTest {

	@Test
	public void allGroupTypesSupported() {
		for(final GroupType groupType : GroupType.values()) {
			try {
				new GroupEntity("Any name", groupType);
			} catch (Exception e) {
				fail(String.format("Group creation failed for group type %s", groupType));
			}
		}
	}
}
