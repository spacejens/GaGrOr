package com.gagror.data;

import static com.gagror.GagrorAssert.assertFieldFinal;
import static com.gagror.GagrorAssert.assertFieldHasGetter;

import java.lang.reflect.Field;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.DesignRulesTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class OutputDesignRulesTest extends DesignRulesTestSupport {

	@SuppressWarnings("unused")
	private final String name;

	private final Class<?> output;

	@Test
	public void allFieldsAreFinal() {
		for(final Field field : output.getDeclaredFields()) {
			assertFieldFinal(field, output, true);
		}
	}

	@Test
	public void allFieldsHaveGetters() {
		for(final Field field : output.getDeclaredFields()) {
			assertFieldHasGetter(field, output);
		}
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findEntities() {
		return parameterizeForAllSubclasses(AbstractEntityOutput.class);
	}
}
