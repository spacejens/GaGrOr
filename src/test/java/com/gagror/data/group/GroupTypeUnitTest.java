package com.gagror.data.group;

import static org.junit.Assert.assertEquals;
import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.EnumUnitTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class GroupTypeUnitTest extends EnumUnitTestSupport<GroupType> {

	@RequiredArgsConstructor
	private enum ExpectedResults {
		SOCIAL(GroupType.SOCIAL, 0),
		WH40K_SKIRMISH(GroupType.WH40K_SKIRMISH, 1);

		private final GroupType groupType;
		private final Integer databaseId;

		public static ExpectedResults forGroupType(final GroupType type) {
			for(final ExpectedResults exp : values()) {
				if(exp.groupType.equals(type)) {
					return exp;
				}
			}
			throw new IllegalArgumentException(String.format("Test has no expected results for %s", type));
		}
	}

	private final GroupType groupType;

	@Test
	public void getId() {
		assertEquals(String.format("ID of group type %s is used in database and may not change", groupType),
				ExpectedResults.forGroupType(groupType).databaseId,
				groupType.getId());
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findInstances() {
		return parameterizeForInstances(GroupType.class);
	}
}
