package com.gagror.data;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractIdentifiableNamedInput<I extends Serializable> extends AbstractIdentifiableInput<I> {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String name;

	protected AbstractIdentifiableNamedInput(final Identifiable<I> currentState) {
		super(currentState);
	}

	// TODO Add constructor getting name from Named (new interface)

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), name);
	}
}
