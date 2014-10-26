package com.gagror.data.group;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.data.AttributeConverterUnitTestSupport;

@RunWith(Parameterized.class)
public class MemberTypeConverterUnitTest extends AttributeConverterUnitTestSupport<Integer, MemberType, MemberTypeConverter> {

	public MemberTypeConverterUnitTest(final MemberType attribute) {
		super(attribute, new MemberTypeConverter());
	}

	@Parameters(name="{0}")
	public static Collection<Object[]> getParameters() throws Exception {
		return getParameters(MemberType.class);
	}
}
