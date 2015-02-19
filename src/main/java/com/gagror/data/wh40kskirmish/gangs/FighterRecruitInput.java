package com.gagror.data.wh40kskirmish.gangs;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gagror.data.AbstractNonIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class FighterRecruitInput
extends AbstractNonIdentifiableNamedInput<FighterReferenceOutput>
implements GroupIdentifiable, GangIdentifiable {

	@Getter
	@Setter
	@NotNull
	private Long groupId;

	@Getter
	@Setter
	@NotNull
	private Long gangId;

	@Getter
	@Setter
	@NotNull
	private Long fighterTypeId;

	public FighterRecruitInput(final Long groupId, final Long gangId) {
		setGroupId(groupId);
		setGangId(gangId);
	}

	public void addErrorNotEnoughMoney(final BindingResult bindingResult, final int cost) {
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(),
				"fighterTypeId",
				String.format("Not enough money, costs %d", cost)));
	}
}
