package com.gagror.data;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractIdentifiableNamedInput<I extends Serializable, C extends Identifiable<I> & Versioned & Named>
extends AbstractIdentifiableInput<I, C>
implements Named {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String name;

	protected AbstractIdentifiableNamedInput(final C currentState) {
		super(currentState);
		setName(currentState.getName());
	}

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), name);
	}
}
