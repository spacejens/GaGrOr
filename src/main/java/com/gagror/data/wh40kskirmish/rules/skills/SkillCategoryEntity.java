package com.gagror.data.wh40kskirmish.rules.skills;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupOwned;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_skillcategory")
public class SkillCategoryEntity extends AbstractEditableNamedEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishRulesEntity rules;

	@OneToMany(mappedBy="skillCategory", fetch=FetchType.LAZY)
	@Getter
	private Set<SkillEntity> skills;

	public SkillCategoryEntity(final Wh40kSkirmishRulesEntity rules) {
		this.rules = rules;
		// Add the new entity to the referencing collection
		rules.getSkillCategories().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getRules().getGroup();
	}
}
