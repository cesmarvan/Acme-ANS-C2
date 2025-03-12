
package acme.entities.flight;

import java.util.Date;

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
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.leg.Leg;
import acme.entities.manager.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
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
	@ValidString(max = 255)
	@Automapped
	private String				description;

	// Derived attributes


	@Transient
	public Date getScheduleDeparture() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		java.util.Optional<Leg> leg = repository.getFisrtLegOfFlight(this.getId());

		return leg.isPresent() ? leg.get().getScheduledDeparture() : null;
	}

	@Transient
	public Date getScheduleArrival() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		java.util.Optional<Leg> leg = repository.getLastLegOfFlight(this.getId());

		return leg.isPresent() ? leg.get().getScheduledArrival() : null;

	}

	@Transient
	public String getOriginCity() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		java.util.Optional<Leg> leg = repository.getFisrtLegOfFlight(this.getId());

		return leg.isPresent() ? leg.get().getDepartureAirport().getCity() : null;
	}

	@Transient
	public String getDestinationCity() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		java.util.Optional<Leg> leg = repository.getLastLegOfFlight(this.getId());

		return leg.isPresent() ? leg.get().getArrivalAirport().getCity() : null;

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
