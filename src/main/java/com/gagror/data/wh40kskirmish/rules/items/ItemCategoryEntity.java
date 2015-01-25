package com.gagror.data.wh40kskirmish.rules.items;

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
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_itemcategory")
public class ItemCategoryEntity extends AbstractEditableNamedEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishRulesEntity rules;

	@OneToMany(mappedBy="itemCategory", fetch=FetchType.LAZY)
	@Getter
	private Set<ItemTypeEntity> itemTypes;

	// TODO An item category can be restricted, with skill or fighter type requirements for access (check through mappings)

	public ItemCategoryEntity(final Wh40kSkirmishRulesEntity rules) {
		this.rules = rules;
		// Add the new entity to the referencing collection
		rules.getItemCategories().add(this);
	}
}
