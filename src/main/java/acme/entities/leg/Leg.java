
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
@Table(indexes = {
	@Index(columnList = "flight_number", unique = true)
})
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	@Column(name = "flight_number", unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;
	// Derived attributes


	@Transient
	public Integer getTravelHours() {
		long differenceMilisec = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();
		long differenceHours = differenceMilisec / (60 * 60 * 1000);
		return Math.toIntExact(differenceHours);
	}

	// Relationships


	@Mandatory
	@Valid
	@ManyToOne
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Aircraft	aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;
}
