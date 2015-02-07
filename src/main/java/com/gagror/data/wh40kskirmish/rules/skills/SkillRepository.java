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
		// TODO Load skill from ID using query, verify group after loading. Category ID no longer needed
		final SkillCategoryEntity skillCategory = skillCategoryRepository.load(groupId, skillCategoryId);
		for(final SkillEntity skill : skillCategory.getSkills()) {
			if(skill.hasId(skillId)) {
				return skill;
			}
		}
		throw new DataNotFoundException(String.format("Skill %d (skill category %d, group %d)",
				skillId, skillCategoryId, groupId));
	}
}
