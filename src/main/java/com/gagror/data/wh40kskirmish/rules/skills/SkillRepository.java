package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class SkillRepository {

	@Autowired
	SkillRepositoryQueries skillRepositoryQueries;

	public SkillEntity persist(final SkillEntity skill) {
		return skillRepositoryQueries.save(skill);
	}

	public SkillEntity load(final Long groupId, final Long skillId) {
		final SkillEntity skill = skillRepositoryQueries.findOne(skillId);
		if(null != skill && skill.getGroup().hasId(groupId)) {
			return skill;
		}
		throw new DataNotFoundException(String.format("Skill %d (group %d)",
				skillId, groupId));
	}
}
