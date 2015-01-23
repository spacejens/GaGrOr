package com.gagror.data.wh40kskirmish.rules.experience;

import java.util.Comparator;

import lombok.Getter;

public class Wh40kSkirmishExperiencePointsComparator implements Comparator<Wh40kSkirmishExperiencePoints> {

	@Getter
	private static Wh40kSkirmishExperiencePointsComparator instance = new Wh40kSkirmishExperiencePointsComparator();

	private Wh40kSkirmishExperiencePointsComparator() {
	}

	@Override
	public int compare(final Wh40kSkirmishExperiencePoints o1, final Wh40kSkirmishExperiencePoints o2) {
		return Integer.compare(o1.getExperiencePoints(), o2.getExperiencePoints());
	}
}
