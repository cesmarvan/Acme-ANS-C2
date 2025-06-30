
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidFlightCrewMember;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhoneNumber;
import acme.entities.airline.Airline;
import acme.entities.crewMember.AvailabilityStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightCrewMember
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidIdentifier
	@Automapped
	@Column(name = "employeeCode", unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private AvailabilityStatus	status;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidNumber(min = 0)
	@Automapped
	private Double				yearsOfExperience;

	@Mandatory
	@Valid
	@ManyToOne
	private Airline				airline;
}
