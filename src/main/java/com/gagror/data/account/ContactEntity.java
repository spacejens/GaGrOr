package com.gagror.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.gagror.data.Identifiable;

@Data
@NoArgsConstructor
@Entity
@Table(name="account")
@EqualsAndHashCode(of="id")
public class ContactEntity implements Identifiable<Long> {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private Long version;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private AccountEntity owner;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private AccountEntity contact;

	@Column(nullable = false)
	private ContactType contactType;
}
