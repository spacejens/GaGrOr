package se.spacejens.gagror.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A simple message entity, for testing persistence framework.
 * 
 * @author spacejens
 */
@Entity
public class Message extends EntitySupport {

	private Long id;

	private String text;

	/**
	 * Constructor needed by persistence framework.
	 */
	Message() {
		this.getLog().debug("Creating instance");
	}

	/**
	 * Constructor setting message contents.
	 * 
	 * @param text
	 *            Message text.
	 */
	public Message(final String text) {
		this.getLog().debug("Creating instance with text \"{}\"", text);
		this.text = text;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	void setId(final Long id) {
		this.getLog().debug("Changing ID from {} to {}", this.id, id);
		this.id = id;
	}

	@NotNull
	@Size(min = 1, max = 255)
	@Column(nullable = false)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.getLog().debug("Changing text from \"{}\" to \"{}\"", this.text, text);
		this.text = text;
	}
}
