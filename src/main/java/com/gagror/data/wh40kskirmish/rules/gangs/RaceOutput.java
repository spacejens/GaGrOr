package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;

public class RaceOutput extends RaceReferenceOutput {

	@Getter
	private final GangTypeOutput gangType;

	@Getter
	private final int maxMovement;

	@Getter
	private final int maxWeaponSkill;

	@Getter
	private final int maxBallisticSkill;

	@Getter
	private final int maxStrength;

	@Getter
	private final int maxToughness;

	@Getter
	private final int maxWounds;

	@Getter
	private final int maxInitiative;

	@Getter
	private final int maxAttacks;

	@Getter
	private final int maxLeadership;

	public RaceOutput(
			final RaceEntity entity,
			final GangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
		maxMovement = entity.getMaxMovement();
		maxWeaponSkill = entity.getMaxWeaponSkill();
		maxBallisticSkill = entity.getMaxBallisticSkill();
		maxStrength = entity.getMaxStrength();
		maxToughness = entity.getMaxToughness();
		maxWounds = entity.getMaxWounds();
		maxInitiative = entity.getMaxInitiative();
		maxAttacks = entity.getMaxAttacks();
		maxLeadership = entity.getMaxLeadership();
	}
}
