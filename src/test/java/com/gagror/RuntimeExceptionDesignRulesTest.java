package com.gagror;

import static org.junit.Assert.assertTrue;
import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class RuntimeExceptionDesignRulesTest extends DesignRulesTestSupport {

	@SuppressWarnings("unused")
	private final String name;

	private final Class<?> clazz;

	@Test
	public void sharedSuperclass() {
		assertSuperclass(clazz, GagrorRuntimeException.class);
	}

	@Test
	public void responseStatus() {
		assertTrue("Should be annotated with @ResponseStatus", clazz.isAnnotationPresent(ResponseStatus.class));
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findExceptions() {
		return parameterizeForConcreteSubclasses(RuntimeException.class);
	}
}
