package com.gagror.data.account;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.data.AttributeConverterUnitTestSupport;

@RunWith(Parameterized.class)
public class ContactTypeConverterUnitTest extends AttributeConverterUnitTestSupport<Integer, ContactType, ContactTypeConverter> {

	public ContactTypeConverterUnitTest(final ContactType attribute) {
		super(attribute, new ContactTypeConverter());
	}

	@Parameters(name="{0}")
	public static Collection<Object[]> getParameters() throws Exception {
		return getParameters(ContactType.class);
	}
}
