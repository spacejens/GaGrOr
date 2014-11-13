package com.gagror.data.group;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@NoArgsConstructor
public class GroupEditInput extends GroupCreateInput {

	private Long id;

	private Long version;

	private boolean viewableByAnyone;

	public GroupEditInput(final GroupEditOutput currentState) {
		setId(currentState.getId());
		setVersion(currentState.getVersion());
		setName(currentState.getName());
		setViewableByAnyone(currentState.isViewableByAnyone());
	}

	public void addErrorSimultaneuosEdit(final BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Simultaneous edit of this group detected, cannot proceed"));
	}
}
