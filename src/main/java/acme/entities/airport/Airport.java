
package acme.entities.airport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidShortText
	@Automapped
	private String					name;

	@Mandatory
	@ValidString(min = 3, max = 3)
	@Column(unique = true)
	private String					iataCode;

	@Mandatory
	@Valid
	@Automapped
	private AirportOperationalScope	operationalScope;

	@Mandatory
	@ValidShortText
	@Automapped
	private String					city;

	@Mandatory
	@ValidShortText
	@Automapped
	private String					country;

	@Optional
	@ValidUrl
	@Automapped
	private String					web;

	@Optional
	@ValidEmail
	@Automapped
	private String					emailAddress;

	@Optional
	@ValidPhoneNumber
	@Automapped
	private String					contactPhoneNumber;

}
