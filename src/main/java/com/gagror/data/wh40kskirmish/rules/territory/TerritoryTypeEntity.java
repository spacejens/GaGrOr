package com.gagror.data.wh40kskirmish.rules.territory;

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
@Table(name="wh40ksk_territorytype")
public class TerritoryTypeEntity extends AbstractEditableNamedEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private TerritoryCategoryEntity territoryCategory;

	public TerritoryTypeEntity(final TerritoryCategoryEntity territoryCategory) {
		this.territoryCategory = territoryCategory;
		// Add the new entity to the referencing collection
		territoryCategory.getTerritoryTypes().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getTerritoryCategory().getGroup();
	}
}
