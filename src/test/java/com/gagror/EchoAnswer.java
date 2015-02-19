package com.gagror;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class EchoAnswer<T> implements Answer<T> {
	// TODO Use EchoAnswer in all tests where this behavior is explicitly mocked

	@SuppressWarnings("unchecked")
	@Override
	public T answer(final InvocationOnMock invocation) throws Throwable {
		return (T)invocation.getArguments()[0];
	}
}
