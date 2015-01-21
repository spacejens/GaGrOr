package com.gagror.data.wh40kskirmish.rules.gangs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishFighterTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishFighterTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

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

	public Wh40kSkirmishFighterTypeInput(final Wh40kSkirmishFighterTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getRace().getGangType().getGroup().getId());
		setGangTypeId(currentState.getRace().getGangType().getId());
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
	}

	public Wh40kSkirmishFighterTypeInput(final Long groupId, final Long gangTypeId, final Long raceId) {
		this.groupId = groupId;
		this.gangTypeId = gangTypeId;
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
	}
}
