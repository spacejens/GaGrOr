package com.gagror.data;

import static com.gagror.GagrorAssert.assertFieldHasGetter;
import static com.gagror.GagrorAssert.assertFieldHasSetter;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Constructor;
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
public class InputDesignRulesTest extends DesignRulesTestSupport {

	private final String name;

	private final Class<?> input;

	@Test
	public void hasNoArgsConstructor() throws Exception {
		final Constructor<?> noArgsConstructor = input.getConstructor();
		assertFalse("No args constructor should not be private (needed to create input from HTTP POST)",
				Modifier.isPrivate(noArgsConstructor.getModifiers()));
	}

	@Test
	public void noFieldsAreFinal() {
		for(final Field field : input.getDeclaredFields()) {
			assertFalse(String.format("Field %s in class %s should not be final", field.getName(), name),
					Modifier.isFinal(field.getModifiers()));
		}
	}

	@Test
	public void allFieldsHaveGetters() {
		for(final Field field : input.getDeclaredFields()) {
			assertFieldHasGetter(field, input);
		}
	}

	@Test
	public void allFieldsHaveSetters() {
		for(final Field field : input.getDeclaredFields()) {
			assertFieldHasSetter(field, input);
		}
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findInputs() {
		return parameterizeForAllSubclasses(AbstractInput.class);
	}
}
