package com.gagror.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractIdentifiableInput<I extends Serializable> extends AbstractInput {

	@Getter
	@Setter
	private I id;

	protected AbstractIdentifiableInput(final Identifiable<I> currentState) {
		setId(currentState.getId());
	}

	// TODO Create abstract class for versioned input, or include version in this class (editable implies versioned)

	@Override
	public String toString() {
		return String.format("%s, id=%d", super.toString(), id);
	}
}
