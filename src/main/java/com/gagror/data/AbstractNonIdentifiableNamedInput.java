package com.gagror.data;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractNonIdentifiableNamedInput extends AbstractInput {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String name;

	// TODO Add constructor getting name from Named (new interface)

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), name);
	}
}
