package com.gagror.data;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Constructor;
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
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findInputs() {
		return parameterizeForConcreteSubclasses(AbstractInput.class);
	}
}
