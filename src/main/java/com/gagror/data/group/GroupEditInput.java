package com.gagror.data.group;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.gagror.data.AbstractIdentifiableNamedInput;

@ToString(callSuper=true)
@NoArgsConstructor
public class GroupEditInput extends AbstractIdentifiableNamedInput<Long> {

	@Getter
	@Setter
	private Long version;

	@Getter
	@Setter
	private boolean viewableByAnyone;

	public GroupEditInput(final GroupEditOutput currentState) {
		super(currentState);
		setVersion(currentState.getVersion());
		setName(currentState.getName());
		setViewableByAnyone(currentState.isViewableByAnyone());
	}

	public void addErrorSimultaneuosEdit(final BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Simultaneous edit of this group detected, cannot proceed"));
	}
}
