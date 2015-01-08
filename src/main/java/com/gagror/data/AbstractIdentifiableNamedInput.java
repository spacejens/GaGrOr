package com.gagror.data;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractIdentifiableNamedInput extends AbstractIdentifiableInput {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String name;

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), name);
	}
}
