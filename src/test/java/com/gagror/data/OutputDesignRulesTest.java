package com.gagror.data;

import static com.gagror.GagrorAssert.assertFieldHasGetter;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.DesignRulesTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class OutputDesignRulesTest extends DesignRulesTestSupport {

	private final String name;

	private final Class<?> output;

	@Test
	public void allFieldsAreFinal() {
		for(final Field field : output.getDeclaredFields()) {
			assertTrue(String.format("Field %s in class %s should be final", field.getName(), name), Modifier.isFinal(field.getModifiers()));
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
