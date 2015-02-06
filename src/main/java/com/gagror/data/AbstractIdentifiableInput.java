package com.gagror.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@NoArgsConstructor
public abstract class AbstractIdentifiableInput<C extends Identifiable<Long> & Versioned>
extends AbstractInput
implements Identifiable<Long>, Versioned {

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private Long version;

	protected AbstractIdentifiableInput(final C currentState) {
		setId(currentState.getId());
		setVersion(currentState.getVersion());
	}

	public void addErrorSimultaneuosEdit(final BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Simultaneous edit detected, cannot proceed"));
	}

	@Override
	public String toString() {
		return String.format("%s, id=%d", super.toString(), id);
	}
}
