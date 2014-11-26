package com.gagror.data.wh40kskirmish;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_gangtype")
public class Wh40kSkirmishGangTypeEntity extends AbstractEditableEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishRulesEntity rules;

	@Column(nullable = false)
	@Getter
	@Setter
	private String name;
}
