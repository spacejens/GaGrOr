package com.gagror.data.account;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.data.AttributeConverterUnitTestSupport;

@RunWith(Parameterized.class)
public class AccountTypeConverterUnitTest extends AttributeConverterUnitTestSupport<Integer, AccountType, AccountTypeConverter> {

	public AccountTypeConverterUnitTest(final AccountType attribute) {
		super(attribute, new AccountTypeConverter());
	}

	@Parameters(name="{0}")
	public static Collection<Object[]> getParameters() throws Exception {
		return getParameters(AccountType.class);
	}
}
