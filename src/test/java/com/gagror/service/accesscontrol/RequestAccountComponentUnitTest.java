package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public class RequestAccountComponentUnitTest {

	@Test
	public void scope() {
		final Scope scope = RequestAccountComponent.class.getAnnotation(Scope.class);
		assertNotNull("Missing scope annotation", scope);
		assertEquals("Wrong scope value", "request", scope.value());
		assertEquals("Wrong scope proxy mode", ScopedProxyMode.TARGET_CLASS, scope.proxyMode());
	}
}
