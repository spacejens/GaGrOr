package com.gagror.service;

import static org.junit.Assert.assertTrue;
import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.stereotype.Service;

import com.gagror.DesignRulesTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class PersisterDesignRulesTest extends DesignRulesTestSupport {

	@SuppressWarnings("unused")
	private final String name;

	private final Class<?> persister;

	@Test
	public void serviceAnnotation() {
		assertTrue("Persister should have @Service", persister.isAnnotationPresent(Service.class));
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findPersisters() {
		return parameterizeForConcreteSubclasses(AbstractPersister.class);
	}
}
