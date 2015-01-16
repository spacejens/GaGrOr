package com.gagror.data;

import static com.gagror.GagrorAssert.assertFieldFinal;
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

	@SuppressWarnings("unused")
	private final String name;

	private final Class<?> input;

	@Test
	public void hasNoArgsConstructor() throws Exception {
		final Constructor<?> noArgsConstructor = input.getConstructor();
		assertFalse("No args constructor should not be private (needed to create input from HTTP POST)",
				Modifier.isPrivate(noArgsConstructor.getModifiers()));
		// Invoke the constructor to verify that it works
		if(! Modifier.isAbstract(input.getModifiers())) {
			noArgsConstructor.newInstance();
		}
	}

	@Test
	public void noFieldsAreFinal() {
		for(final Field field : input.getDeclaredFields()) {
			assertFieldFinal(field, input, false);
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
