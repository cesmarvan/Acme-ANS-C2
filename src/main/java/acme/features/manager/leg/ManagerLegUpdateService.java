
package acme.features.manager.leg;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Manager manager;
		int legId;
		Leg leg;

		if (super.getRequest().hasData("id", int.class)) {
			legId = super.getRequest().getData("id", int.class);
			leg = this.repository.findLegById(legId);
			if (leg == null)
				manager = null;
			else
				manager = leg.getFlight().getManager();
		} else {
			leg = null;
			manager = null;
		}

		status = leg != null && super.getRequest().getPrincipal().hasRealm(manager) && leg.getDraftMode();

		if (status) {
			int aircraftId, arrivalAirportId, departureAirportId;

			aircraftId = super.getRequest().getData("aircraft", int.class);
			arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
			departureAirportId = super.getRequest().getData("departureAirport", int.class);

			Aircraft aircraft;
			Airport arrivalAirport;
			Airport departureAirport;
			aircraft = this.repository.findAircraftById(aircraftId);
			arrivalAirport = this.repository.findAirportById(arrivalAirportId);
			departureAirport = this.repository.findAirportById(departureAirportId);

			if (!(aircraftId == 0 || aircraft != null))
				status = false;
			else if (!(arrivalAirportId == 0 || arrivalAirport != null))
				status = false;
			else if (!(departureAirportId == 0 || departureAirport != null))
				status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int legId;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		Flight flight;

		flight = leg.getFlight();

		int aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		int departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);

		int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		Airport arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		if (aircraft == null)
			leg.setFlightNumber(leg.getFlightNumber());
		else
			leg.setFlightNumber(aircraft.getAirline().getIataCode() + leg.getFlightNumber());
		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
	}

	@Override
	public void validate(final Leg leg) {
		{
			boolean datesNotNull = true;

			if (leg.getScheduledArrival() == null) {
				datesNotNull = false;
				super.state(datesNotNull, "scheduledArrival", "acme.validation.leg.null-dates.message");
			}
			if (leg.getScheduledDeparture() == null) {
				datesNotNull = false;
				super.state(datesNotNull, "scheduledDeparture", "acme.validation.leg.null-dates.message");
			}

			super.state(datesNotNull, "scheduledDeparture", "acme.validation.leg.null-dates.message");
		}
		{
			boolean isInFuture = true;

			Date now = MomentHelper.getBaseMoment();

			if (leg.getScheduledDeparture() != null)
				isInFuture = leg.getScheduledDeparture().after(now);

			super.state(isInFuture, "scheduledDeparture", "acme.validation.leg.not-in-future-date.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		Flight flight = leg.getFlight();

		SelectChoices statusChoices;
		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		SelectChoices aircraftChoices;
		List<Aircraft> aircrafts = this.repository.findActivesAircrafts(AircraftStatus.ACTIVE_SERVICE);
		List<Aircraft> ableAircrafts = aircrafts.stream().filter(a -> a.getStatus().equals(AircraftStatus.ACTIVE_SERVICE)).toList();
		aircraftChoices = SelectChoices.from(ableAircrafts, "registrationNumber", leg.getAircraft());

		SelectChoices airportDepartureChoices;
		SelectChoices airportArrivalChoices;
		List<Airport> airports = this.repository.findAllAirports();
		airportDepartureChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		airportArrivalChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");

		Aircraft aircraft = this.repository.findAircraftById(Integer.valueOf(aircraftChoices.getSelected().getKey()));
		if (aircraft != null) {
			String fNumber = leg.getFlightNumber().replace(aircraft.getAirline().getIataCode(), "");
			dataset.put("flightNumber", fNumber);
		}

		dataset.put("status", statusChoices);
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("departureAirports", airportDepartureChoices);
		dataset.put("departureAirport", airportDepartureChoices.getSelected().getKey());
		dataset.put("arrivalAirports", airportArrivalChoices);
		dataset.put("arrivalAirport", airportArrivalChoices.getSelected().getKey());
		dataset.put("flight", flight);

		super.getResponse().addData(dataset);

	}

}
