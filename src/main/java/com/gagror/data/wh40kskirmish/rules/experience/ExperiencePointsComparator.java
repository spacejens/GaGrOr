package com.gagror.data.wh40kskirmish.rules.experience;

import java.util.Comparator;

import lombok.Getter;

public class ExperiencePointsComparator implements Comparator<ExperiencePoints> {

	@Getter
	private static ExperiencePointsComparator instance = new ExperiencePointsComparator();

	private ExperiencePointsComparator() {
	}

	@Override
	public int compare(final ExperiencePoints o1, final ExperiencePoints o2) {
		return Integer.compare(o1.getExperiencePoints(), o2.getExperiencePoints());
	}
}
