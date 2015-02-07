package com.gagror.data.wh40kskirmish.rules.gangs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class FighterTypeInput
extends AbstractIdentifiableNamedInput<FighterTypeOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long raceId;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingMovement;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingWeaponSkill;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingBallisticSkill;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingStrength;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingToughness;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingWounds;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingInitiative;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingAttacks;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int startingLeadership;

	@Getter
	@Setter
	@Min(1)
	private int cost;

	public FighterTypeInput(final FighterTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getRace().getGangType().getGroup().getId());
		setRaceId(currentState.getRace().getId());
		setStartingMovement(currentState.getStartingMovement());
		setStartingWeaponSkill(currentState.getStartingWeaponSkill());
		setStartingBallisticSkill(currentState.getStartingBallisticSkill());
		setStartingStrength(currentState.getStartingStrength());
		setStartingToughness(currentState.getStartingToughness());
		setStartingWounds(currentState.getStartingWounds());
		setStartingInitiative(currentState.getStartingInitiative());
		setStartingAttacks(currentState.getStartingAttacks());
		setStartingLeadership(currentState.getStartingLeadership());
		setCost(currentState.getCost());
	}

	public FighterTypeInput(final Long groupId, final Long raceId) {
		this.groupId = groupId;
		this.raceId = raceId;
		// Default values for mandatory fields
		setStartingMovement(4);
		setStartingWeaponSkill(3);
		setStartingBallisticSkill(3);
		setStartingStrength(3);
		setStartingToughness(3);
		setStartingWounds(1);
		setStartingInitiative(3);
		setStartingAttacks(1);
		setStartingLeadership(7);
		setCost(20);
	}

	public void addErrorStartingAboveMaxMovement(final BindingResult bindingResult, final int maxMovement) {
		addErrorStartingAboveMax(bindingResult, "startingMovement", maxMovement);
	}

	public void addErrorStartingAboveMaxWeaponSkill(final BindingResult bindingResult, final int maxWeaponSkill) {
		addErrorStartingAboveMax(bindingResult, "startingWeaponSkill", maxWeaponSkill);
	}

	public void addErrorStartingAboveMaxBallisticSkill(final BindingResult bindingResult, final int maxBallisticSkill) {
		addErrorStartingAboveMax(bindingResult, "startingBallisticSkill", maxBallisticSkill);
	}

	public void addErrorStartingAboveMaxStrength(final BindingResult bindingResult, final int maxStrength) {
		addErrorStartingAboveMax(bindingResult, "startingStrength", maxStrength);
	}

	public void addErrorStartingAboveMaxToughness(final BindingResult bindingResult, final int maxToughness) {
		addErrorStartingAboveMax(bindingResult, "startingToughness", maxToughness);
	}

	public void addErrorStartingAboveMaxWounds(final BindingResult bindingResult, final int maxWounds) {
		addErrorStartingAboveMax(bindingResult, "startingWounds", maxWounds);
	}

	public void addErrorStartingAboveMaxInitiative(final BindingResult bindingResult, final int maxInitiative) {
		addErrorStartingAboveMax(bindingResult, "startingInitiative", maxInitiative);
	}

	public void addErrorStartingAboveMaxAttacks(final BindingResult bindingResult, final int maxAttacks) {
		addErrorStartingAboveMax(bindingResult, "startingAttacks", maxAttacks);
	}

	public void addErrorStartingAboveMaxLeadership(final BindingResult bindingResult, final int maxLeadership) {
		addErrorStartingAboveMax(bindingResult, "startingLeadership", maxLeadership);
	}

	private void addErrorStartingAboveMax(final BindingResult bindingResult, final String field, final int max) {
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(),
				field,
				String.format("above racial maximum %d", max)));
	}
}
