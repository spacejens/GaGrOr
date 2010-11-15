package se.spacejens.gagror.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A simple message entity, for testing persistence framework.
 * 
 * @author spacejens
 */
@Entity
public class Message {

	private Long id;

	private String text;

	/**
	 * Constructor needed by persistence framework.
	 */
	Message() {
		// Do nothing
	}

	/**
	 * Constructor setting message contents.
	 * 
	 * @param text
	 *            Message text.
	 */
	public Message(final String text) {
		this.text = text;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	void setId(final Long id) {
		this.id = id;
	}

	@Column
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
