package com.gagror.service.wh40kskirmish.gangs;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterViewOutput;

@Service
@Transactional
public class FighterViewService {

	public FighterViewOutput view(final FighterEntity entity) {
		final FighterViewOutput.Builder builder = new FighterViewOutput.Builder(entity);
		// TODO Add experience level title to the fighter view output
		calculateCharacteristics(entity, builder);
		calculateCost(entity, builder);
		return builder.build();
	}

	protected void calculateCharacteristics(final FighterEntity entity, final FighterViewOutput.Builder builder) {
		// Initialize the characteristics from the fighter type
		builder.setMovement(entity.getFighterType().getStartingMovement());
		builder.setWeaponSkill(entity.getFighterType().getStartingWeaponSkill());
		builder.setBallisticSkill(entity.getFighterType().getStartingBallisticSkill());
		builder.setStrength(entity.getFighterType().getStartingStrength());
		builder.setToughness(entity.getFighterType().getStartingToughness());
		builder.setWounds(entity.getFighterType().getStartingWounds());
		builder.setInitiative(entity.getFighterType().getStartingInitiative());
		builder.setAttacks(entity.getFighterType().getStartingAttacks());
		builder.setLeadership(entity.getFighterType().getStartingLeadership());
		// TODO Modify fighter characteristics by experience advances
	}

	protected void calculateCost(final FighterEntity entity, final FighterViewOutput.Builder builder) {
		// Initialize cost from the fighter type
		builder.setCost(entity.getFighterType().getCost());
		// TODO Modify fighter cost by the carried items
	}
}
