package com.gagror.data;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractNonIdentifiableNamedInput<C extends Named>
implements Named, Input {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String name;

	protected AbstractNonIdentifiableNamedInput(final C currentState) {
		setName(currentState.getName());
	}

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), name);
	}
}
