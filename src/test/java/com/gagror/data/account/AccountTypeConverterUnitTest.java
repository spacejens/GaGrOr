package com.gagror.data.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collection;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class AccountTypeConverterUnitTest {

	private final AccountType accountType;

	private final AccountTypeConverter instance = new AccountTypeConverter();

	@Test
	public void convertToDatabaseColumn() {
		assertEquals(accountType.getId(), instance.convertToDatabaseColumn(accountType).intValue());
	}

	@Test
	public void convertToEntityAttribute() {
		assertSame(accountType, instance.convertToEntityAttribute(accountType.getId()));
	}

	@Parameters(name="{0}")
	public static Collection<Object[]> getParameters() throws Exception {
		// TODO Move enum test parameterization to shared utility class
		final ArrayList<Object[]> output = new ArrayList<>();
		for(final AccountType accountType : AccountType.values()) {
			output.add(new Object[]{accountType});
		}
		return output;
	}
}
