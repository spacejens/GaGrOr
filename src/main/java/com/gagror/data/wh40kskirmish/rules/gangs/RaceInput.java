package com.gagror.data.wh40kskirmish.rules.gangs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class RaceInput extends AbstractIdentifiableNamedInput<Long, RaceOutput> {

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
}
