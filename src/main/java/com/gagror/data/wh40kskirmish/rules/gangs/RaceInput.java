package com.gagror.data.wh40kskirmish.rules.gangs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class RaceInput extends AbstractIdentifiableNamedInput<RaceOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxMovement;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxWeaponSkill;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxBallisticSkill;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxStrength;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxToughness;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxWounds;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxInitiative;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxAttacks;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int maxLeadership;

	public RaceInput(final RaceOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGangType().getGroup().getId());
		setGangTypeId(currentState.getGangType().getId());
		setMaxMovement(currentState.getMaxMovement());
		setMaxWeaponSkill(currentState.getMaxWeaponSkill());
		setMaxBallisticSkill(currentState.getMaxBallisticSkill());
		setMaxStrength(currentState.getMaxStrength());
		setMaxToughness(currentState.getMaxToughness());
		setMaxWounds(currentState.getMaxWounds());
		setMaxInitiative(currentState.getMaxInitiative());
		setMaxAttacks(currentState.getMaxAttacks());
		setMaxLeadership(currentState.getMaxLeadership());
	}

	public RaceInput(final Long groupId, final Long gangTypeId) {
		setGroupId(groupId);
		setGangTypeId(gangTypeId);
		// Default values for mandatory fields
		setMaxMovement(4);
		setMaxWeaponSkill(6);
		setMaxBallisticSkill(6);
		setMaxStrength(4);
		setMaxToughness(4);
		setMaxWounds(3);
		setMaxInitiative(6);
		setMaxAttacks(3);
		setMaxLeadership(9);
	}

	public void addErrorMaxBelowStartingMovement(final BindingResult bindingResult, final int startingMovement) {
		addErrorMaxBelowStarting(bindingResult, "maxMovement", startingMovement);
	}

	public void addErrorMaxBelowStartingWeaponSkill(final BindingResult bindingResult, final int startingWeaponSkill) {
		addErrorMaxBelowStarting(bindingResult, "maxWeaponSkill", startingWeaponSkill);
	}

	public void addErrorMaxBelowStartingBallisticSkill(final BindingResult bindingResult, final int startingBallisticSkill) {
		addErrorMaxBelowStarting(bindingResult, "maxBallisticSkill", startingBallisticSkill);
	}

	public void addErrorMaxBelowStartingStrength(final BindingResult bindingResult, final int startingStrength) {
		addErrorMaxBelowStarting(bindingResult, "maxStrength", startingStrength);
	}

	public void addErrorMaxBelowStartingToughness(final BindingResult bindingResult, final int startingToughness) {
		addErrorMaxBelowStarting(bindingResult, "maxToughness", startingToughness);
	}

	public void addErrorMaxBelowStartingWounds(final BindingResult bindingResult, final int startingWounds) {
		addErrorMaxBelowStarting(bindingResult, "maxWounds", startingWounds);
	}

	public void addErrorMaxBelowStartingInitiative(final BindingResult bindingResult, final int startingInitiative) {
		addErrorMaxBelowStarting(bindingResult, "maxInitiative", startingInitiative);
	}

	public void addErrorMaxBelowStartingAttacks(final BindingResult bindingResult, final int startingAttacks) {
		addErrorMaxBelowStarting(bindingResult, "maxAttacks", startingAttacks);
	}

	public void addErrorMaxBelowStartingLeadership(final BindingResult bindingResult, final int startingLeadership) {
		addErrorMaxBelowStarting(bindingResult, "maxLeadership", startingLeadership);
	}

	private void addErrorMaxBelowStarting(final BindingResult bindingResult, final String field, final int starting) {
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(),
				field,
				String.format("below fighter type maximum %d", starting)));
	}
}
