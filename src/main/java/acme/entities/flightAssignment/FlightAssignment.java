
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLongText;
import acme.entities.crewMember.FlightCrewMember;
import acme.entities.leg.Leg;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private CrewDuties			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				lastUpdate;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private AssignmentStatus	status;

	@Optional
	@ValidLongText
	@Automapped
	private String				remarks;

	@Mandatory
	@Valid
	@OneToOne
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Valid
	@OneToOne
	private Leg					leg;
}
