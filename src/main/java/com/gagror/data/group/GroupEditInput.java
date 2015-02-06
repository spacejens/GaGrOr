package com.gagror.data.group;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gagror.data.AbstractIdentifiableNamedInput;

@ToString(callSuper=true)
@NoArgsConstructor
public class GroupEditInput extends AbstractIdentifiableNamedInput<GroupEditOutput> {

	@Getter
	@Setter
	private boolean viewableByAnyone;

	public GroupEditInput(final GroupEditOutput currentState) {
		super(currentState);
		setViewableByAnyone(currentState.isViewableByAnyone());
	}

	public void addErrorNameMustBeUniqueWhenViewableByAnyone(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "name", "must be unique for public groups"));
	}
}
