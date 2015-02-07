package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class SkillRepository {

	@Autowired
	SkillRepositoryQueries skillRepositoryQueries;

	@Autowired
	SkillCategoryRepository skillCategoryRepository;

	public SkillEntity persist(final SkillEntity skill) {
		return skillRepositoryQueries.save(skill);
	}

	public SkillEntity load(final Long groupId, final Long skillCategoryId, final Long skillId) {
		// TODO Remove skill category argument when loading skill
		final SkillEntity skill = skillRepositoryQueries.findOne(skillId);
		if(null != skill && skill.getGroup().hasId(groupId)) {
			return skill;
		}
		throw new DataNotFoundException(String.format("Skill %d (skill category %d, group %d)",
				skillId, skillCategoryId, groupId));
	}
}
