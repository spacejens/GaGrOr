package com.gagror.data.account;

import static org.junit.Assert.assertEquals;
import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.EnumUnitTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class ContactTypeUnitTest extends EnumUnitTestSupport<ContactType> {

	@RequiredArgsConstructor
	private enum ExpectedResults {
		BLOCKED(ContactType.BLOCKED, 0),
		REQUESTED(ContactType.REQUESTED, 1),
		AUTOMATIC(ContactType.AUTOMATIC, 2),
		APPROVED(ContactType.APPROVED, 3);

		private final ContactType contactType;
		private final Integer databaseId;

		public static ExpectedResults forContactType(final ContactType type) {
			for(final ExpectedResults exp : values()) {
				if(exp.contactType.equals(type)) {
					return exp;
				}
			}
			throw new IllegalArgumentException(String.format("Test has no expected results for %s", type));
		}
	}

	private final ContactType contactType;

	@Test
	public void getId() {
		assertEquals(String.format("ID of contact type %s is used in database and may not change", contactType),
				ExpectedResults.forContactType(contactType).databaseId,
				contactType.getId());
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findInstances() {
		return parameterizeForInstances(ContactType.class);
	}
}
