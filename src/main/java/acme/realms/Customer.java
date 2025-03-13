
package acme.realms;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidPhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidIdentifier
	@Automapped
	private String				identifier;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString
	@Automapped
	private String				address;

	@Mandatory
	@ValidString
	@Automapped
	private String				city;

	@Mandatory
	@ValidString
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(min = 0, max = 500000)
	@Automapped
	private Integer				earnedPoints;

}
