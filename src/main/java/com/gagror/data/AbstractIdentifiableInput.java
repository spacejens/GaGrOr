package com.gagror.data;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractIdentifiableInput extends AbstractInput {

	@Getter
	@Setter
	private Long id;

	// TODO Create abstract class for versioned input, or include version in this class (editable implies versioned)

	@Override
	public String toString() {
		return String.format("%s, id=%d", super.toString(), id);
	}
}
