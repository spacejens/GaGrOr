package com.gagror.data.wh40kskirmish.gangs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.group.PlayerOwnedOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeReferenceOutput;

public class FighterViewOutput extends FighterReferenceOutput implements PlayerOwnedOutput {

	@Getter
	private final FighterTypeReferenceOutput fighterType;

	@Getter
	private final GangOutput gang;

	@Getter
	private final int movement;

	@Getter
	private final int weaponSkill;

	@Getter
	private final int ballisticSkill;

	@Getter
	private final int strength;

	@Getter
	private final int toughness;

	@Getter
	private final int wounds;

	@Getter
	private final int initiative;

	@Getter
	private final int attacks;

	@Getter
	private final int leadership;

	@Getter
	private final int cost;

	protected FighterViewOutput(final Builder builder) {
		super(builder.getEntity());
		fighterType = new FighterTypeReferenceOutput(builder.getEntity().getFighterType());
		this.gang = builder.getGang();
		this.movement = builder.getMovement();
		this.weaponSkill = builder.getWeaponSkill();
		this.ballisticSkill = builder.getBallisticSkill();
		this.strength = builder.getStrength();
		this.toughness = builder.getToughness();
		this.wounds = builder.getWounds();
		this.initiative = builder.getInitiative();
		this.attacks = builder.getAttacks();
		this.leadership = builder.getLeadership();
		this.cost = builder.getCost();
	}

	@RequiredArgsConstructor
	public static class Builder {

		@Getter(AccessLevel.PRIVATE)
		final FighterEntity entity;

		@Getter(AccessLevel.PRIVATE)
		final GangOutput gang;

		@Getter
		@Setter
		private int movement;

		@Getter
		@Setter
		private int weaponSkill;

		@Getter
		@Setter
		private int ballisticSkill;

		@Getter
		@Setter
		private int strength;

		@Getter
		@Setter
		private int toughness;

		@Getter
		@Setter
		private int wounds;

		@Getter
		@Setter
		private int initiative;

		@Getter
		@Setter
		private int attacks;

		@Getter
		@Setter
		private int leadership;

		@Getter
		@Setter
		private int cost;

		public FighterViewOutput build() {
			return new FighterViewOutput(this);
		}
	}

	@Override
	public AccountReferenceOutput getPlayer() {
		return getGang().getPlayer();
	}
}
