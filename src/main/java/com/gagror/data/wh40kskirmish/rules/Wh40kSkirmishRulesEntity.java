package com.gagror.data.wh40kskirmish.rules;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupOwned;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_rules")
public class Wh40kSkirmishRulesEntity extends AbstractEditableEntity implements GroupOwned {

	@OneToOne(optional=false)
	@Getter
	GroupEntity group;

	@OneToMany(mappedBy="rules", fetch=FetchType.LAZY)
	@Getter
	private Set<GangTypeEntity> gangTypes;

	@OneToMany(mappedBy="rules", fetch=FetchType.LAZY)
	@Getter
	private Set<TerritoryCategoryEntity> territoryCategories;

	@OneToMany(mappedBy="rules", fetch=FetchType.LAZY)
	@Getter
	private Set<SkillCategoryEntity> skillCategories;

	@OneToMany(mappedBy="rules", fetch=FetchType.LAZY)
	@Getter
	private Set<ItemCategoryEntity> itemCategories;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int startingMoney;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private String currencyName;

	public Wh40kSkirmishRulesEntity(final GroupEntity group) {
		this.group = group;
		// Set defaults for mandatory fields
		startingMoney = 0;
		currencyName = "$";
	}
}
