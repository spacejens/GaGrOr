package com.gagror.data.wh40kskirmish.rules.gangs;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_startingterritory")
public class StartingTerritoryEntity extends AbstractEditableEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private GangTypeEntity gangType;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private TerritoryTypeEntity territoryType;

	public StartingTerritoryEntity(final GangTypeEntity gangType, final TerritoryTypeEntity territoryType) {
		this.gangType = gangType;
		this.territoryType = territoryType;
		// Add the new entity to the referencing collection
		gangType.getStartingTerritories().add(this);
	}
}
