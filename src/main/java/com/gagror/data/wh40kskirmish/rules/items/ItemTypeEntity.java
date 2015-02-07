package com.gagror.data.wh40kskirmish.rules.items;

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
@Table(name="wh40ksk_itemtype")
public class ItemTypeEntity extends AbstractEditableNamedEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private ItemCategoryEntity itemCategory;

	public ItemTypeEntity(final ItemCategoryEntity itemCategory) {
		this.itemCategory = itemCategory;
		// Add the new entity to the referencing collection
		itemCategory.getItemTypes().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getItemCategory().getGroup();
	}
}
