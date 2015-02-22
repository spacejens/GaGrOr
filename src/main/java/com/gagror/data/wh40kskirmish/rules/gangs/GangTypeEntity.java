package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.HashSet;
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
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_gangtype")
public class GangTypeEntity extends AbstractEditableNamedEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishRulesEntity rules;

	@OneToMany(mappedBy="gangType", fetch=FetchType.LAZY)
	@Getter
	private Set<FactionEntity> factions;

	@OneToMany(mappedBy="gangType", fetch=FetchType.LAZY)
	@Getter
	private Set<RaceEntity> races;

	@OneToMany(mappedBy="gangType", fetch=FetchType.LAZY)
	@Getter
	private Set<ExperienceLevelEntity> experienceLevels;

	@OneToMany(mappedBy="gangType", fetch=FetchType.LAZY)
	@Getter
	private Set<StartingTerritoryEntity> startingTerritories;
	// TODO Make initial territory allocation configurable (set of territory categories with duplicates, one random from each)

	public GangTypeEntity(final Wh40kSkirmishRulesEntity rules) {
		this.rules = rules;
		// Add the new entity to the referencing collection
		rules.getGangTypes().add(this);
		// Initialize empty collections
		factions = new HashSet<>();
		races = new HashSet<>();
		experienceLevels = new HashSet<>();
		startingTerritories = new HashSet<>();
	}

	@Override
	public GroupEntity getGroup() {
		return getRules().getGroup();
	}
}
