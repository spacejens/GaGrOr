package com.gagror.data.wh40kskirmish.rules.gangs;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupOwned;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_startingterritory")
public class StartingTerritoryEntity extends AbstractEditableEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private GangTypeEntity gangType;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private TerritoryCategoryEntity territoryCategory;

	public StartingTerritoryEntity(final GangTypeEntity gangType, final TerritoryCategoryEntity territoryCategory) {
		this.gangType = gangType;
		this.territoryCategory = territoryCategory;
		// Add the new entity to the referencing collection
		gangType.getStartingTerritories().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getGangType().getGroup();
	}
}
