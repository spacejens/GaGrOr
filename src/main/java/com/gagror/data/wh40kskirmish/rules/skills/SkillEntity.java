package com.gagror.data.wh40kskirmish.rules.skills;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupOwned;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_skill")
public class SkillEntity extends AbstractEditableNamedEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private SkillCategoryEntity skillCategory;

	// TODO Skills grant access to item categories (e.g. Specialist)

	public SkillEntity(final SkillCategoryEntity skillCategory) {
		this.skillCategory = skillCategory;
		// Add the new entity to the referencing collection
		skillCategory.getSkills().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getSkillCategory().getGroup();
	}
}
