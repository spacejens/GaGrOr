package com.gagror.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@NoArgsConstructor
public abstract class AbstractIdentifiableInput<C extends IdentifiablePersistent & Versioned>
extends AbstractIdentifiable
implements VersionedInput {

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

	@Override
	public void addErrorSimultaneuosEdit(final BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Simultaneous edit detected, cannot proceed"));
	}
}
