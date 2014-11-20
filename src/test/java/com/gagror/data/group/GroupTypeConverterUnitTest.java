package com.gagror.data.group;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.data.AttributeConverterUnitTestSupport;

@RunWith(Parameterized.class)
public class GroupTypeConverterUnitTest extends AttributeConverterUnitTestSupport<Integer, GroupType, GroupTypeConverter> {

	public GroupTypeConverterUnitTest(final GroupType attribute) {
		super(attribute, new GroupTypeConverter());
	}

	@Parameters(name="{0}")
	public static Collection<Object[]> getParameters() throws Exception {
		return getParameters(GroupType.class);
	}
}
