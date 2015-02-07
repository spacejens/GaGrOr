package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

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
		// TODO Load skill category from ID using query, verify group after loading
		final Wh40kSkirmishRulesEntity rules = rulesRepository.load(groupId);
		for(final SkillCategoryEntity skillCategory : rules.getSkillCategories()) {
			if(skillCategory.hasId(skillCategoryId)) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", skillCategoryId, groupId));
	}
}
