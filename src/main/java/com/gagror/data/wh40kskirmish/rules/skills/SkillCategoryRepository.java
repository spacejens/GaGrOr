package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;

@Repository
public class SkillCategoryRepository {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	SkillCategoryRepositoryQueries skillCategoryRepositoryQueries;

	public SkillCategoryEntity persist(final SkillCategoryEntity skillCategory) {
		return skillCategoryRepositoryQueries.save(skillCategory);
	}

	public SkillCategoryEntity load(final Long groupId, final Long skillCategoryId) {
		final SkillCategoryEntity skillCategory = skillCategoryRepositoryQueries.findOne(skillCategoryId);
		if(null != skillCategory && skillCategory.getGroup().hasId(groupId)) {
			return skillCategory;
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", skillCategoryId, groupId));
	}
}
