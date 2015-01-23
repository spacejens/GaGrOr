package com.gagror.data.wh40kskirmish.rules.skills;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_skill")
public class Wh40kSkirmishSkillEntity extends AbstractEditableNamedEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishSkillCategoryEntity skillCategory;

	// TODO Skills grant access to item categories (e.g. Specialist)

	public Wh40kSkirmishSkillEntity(final Wh40kSkirmishSkillCategoryEntity skillCategory) {
		this.skillCategory = skillCategory;
		// Add the new entity to the referencing collection
		skillCategory.getSkills().add(this);
	}
}
