package com.gagror;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public class AddError implements Answer<Void> {

	public static Stubber to(final BindingResult bindingResult) {
		return doAnswer(new AddError(bindingResult));
	}

	private final BindingResult bindingResult;

	@Override
	public Void answer(final InvocationOnMock invocation) throws Throwable {
		when(bindingResult.hasErrors()).thenReturn(true);
		return null;
	}
}
