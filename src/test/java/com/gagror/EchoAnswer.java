package com.gagror;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class EchoAnswer<T> implements Answer<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T answer(final InvocationOnMock invocation) throws Throwable {
		return (T)invocation.getArguments()[0];
	}
}
