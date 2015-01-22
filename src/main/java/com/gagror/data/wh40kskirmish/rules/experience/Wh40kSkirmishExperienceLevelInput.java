package com.gagror.data.wh40kskirmish.rules.experience;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractNonIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishExperienceLevelInput
extends AbstractNonIdentifiableNamedInput<Wh40kSkirmishExperienceLevelOutput>
implements ExperiencePoints {

	@Getter
	@Setter
	@Min(0)
	@Max(1000)
	private int experiencePoints;

	public Wh40kSkirmishExperienceLevelInput(final Wh40kSkirmishExperienceLevelOutput currentState) {
		super(currentState);
		setExperiencePoints(currentState.getExperiencePoints());
	}
}
