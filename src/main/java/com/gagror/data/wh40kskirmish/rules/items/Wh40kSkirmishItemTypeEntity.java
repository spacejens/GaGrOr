package com.gagror.data.wh40kskirmish.rules.items;

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
@Table(name="wh40ksk_itemtype")
public class Wh40kSkirmishItemTypeEntity extends AbstractEditableNamedEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishItemCategoryEntity itemCategory;

	public Wh40kSkirmishItemTypeEntity(final Wh40kSkirmishItemCategoryEntity itemCategory) {
		this.itemCategory = itemCategory;
		// Add the new entity to the referencing collection
		itemCategory.getItemTypes().add(this);
	}
}
