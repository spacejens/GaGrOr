package com.gagror.data.group;

import static org.junit.Assert.assertEquals;
import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.EnumUnitTestSupport;
import com.gagror.TestSetupException;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class MemberTypeUnitTest extends EnumUnitTestSupport<MemberType> {

	@RequiredArgsConstructor
	private enum ExpectedResults {
		INVITED(MemberType.INVITED, 0),
		MEMBER(MemberType.MEMBER, 1),
		OWNER(MemberType.OWNER, 2);

		private final MemberType memberType;
		private final Integer databaseId;

		public static ExpectedResults forMemberType(final MemberType type) {
			for(final ExpectedResults exp : values()) {
				if(exp.memberType.equals(type)) {
					return exp;
				}
			}
			throw new TestSetupException(String.format("Test has no expected results for %s", type));
		}
	}

	private final MemberType memberType;

	@Test
	public void getId() {
		assertEquals(String.format("ID of member type %s is used in database and may not change", memberType),
				ExpectedResults.forMemberType(memberType).databaseId,
				memberType.getId());
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findInstances() {
		return parameterizeForInstances(MemberType.class);
	}
}
