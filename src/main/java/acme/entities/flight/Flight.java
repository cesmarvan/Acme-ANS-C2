
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.leg.Leg;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@ValidMoney(min = 0.00, max = 100000000.00)
	@Automapped
	private Money				cost;

	@Optional
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes


	@Transient
	public Date getScheduleDeparture() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getFisrtLegOfFlight(this.getId());

		return legs != null && !legs.isEmpty() ? legs.get(0).getScheduledDeparture() : null;
	}

	@Transient
	public Date getScheduleArrival() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLastLegOfFlight(this.getId());

		return legs != null && !legs.isEmpty() ? legs.get(0).getScheduledArrival() : null;

	}

	@Transient
	public String getOriginCity() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getFisrtLegOfFlight(this.getId());

		return legs != null && !legs.isEmpty() ? legs.get(0).getDepartureAirport().getCity() : null;
	}

	@Transient
	public String getDestinationCity() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLastLegOfFlight(this.getId());

		return legs != null && !legs.isEmpty() ? legs.get(0).getArrivalAirport().getCity() : null;

	}

	@Transient
	public Integer getNumberLavoyers() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		return repository.getNumberOfLegsOfFlight(this.getId());
	}

	// Relationships


	@Mandatory
	@Valid
	@ManyToOne
	private Manager manager;

}
