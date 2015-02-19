package com.gagror.data;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@NoArgsConstructor
public abstract class AbstractIdentifiableNamedInput<C extends IdentifiablePersistent & Versioned & Named>
extends AbstractIdentifiableInput<C>
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

	public void addErrorNameMustBeUniqueWithinGroup(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "name", "must be unique in group"));
	}
}
