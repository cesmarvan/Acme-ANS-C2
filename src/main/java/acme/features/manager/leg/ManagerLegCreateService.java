
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// Abstract Gui Interface -------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		Manager manager;
		manager = this.repository.findFlightById(masterId).getManager();

		status = manager != null && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		flight = this.repository.findFlightById(masterId);

		leg = new Leg();
		leg.setDraftMode(true);
		leg.setFlight(flight);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);

		flight = this.repository.findFlightById(masterId);

		int aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		int departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);

		int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		Airport arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
	}

	@Override
	public void validate(final Leg leg) {
		{
			boolean datesNotNull = true;

			datesNotNull = !(leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null);

			super.state(datesNotNull, "scheduledDeparture", "acme.validation.leg.null-dates.message");
		}
		{
			boolean aircraftNotNull = true;

			aircraftNotNull = leg.getAircraft() != null;

			super.state(aircraftNotNull, "aircraft", "acme.validation.leg.null-aircraft.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(masterId);

		SelectChoices statusChoices;
		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		SelectChoices aircraftChoices;
		List<Aircraft> aircrafts = this.repository.findAllAircrafts();
		List<Aircraft> ableAircrafts = aircrafts.stream().filter(a -> a.getStatus().equals(AircraftStatus.ACTIVE_SERVICE)).toList();
		aircraftChoices = SelectChoices.from(ableAircrafts, "registrationNumber", leg.getAircraft());

		SelectChoices airportDepartureChoices;
		SelectChoices airportArrivalChoices;
		List<Airport> airports = this.repository.findAllAirports();
		airportDepartureChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		airportArrivalChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");

		dataset.put("status", statusChoices);
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("departureAirports", airportDepartureChoices);
		dataset.put("departureAirport", airportDepartureChoices.getSelected().getKey());
		dataset.put("arrivalAirports", airportArrivalChoices);
		dataset.put("flight", flight);

		super.getResponse().addData(dataset);

	}

}
