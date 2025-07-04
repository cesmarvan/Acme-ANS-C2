
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
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface --------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Leg leg;
		Manager manager;

		if (super.getRequest().hasData("id", int.class)) {
			id = super.getRequest().getData("id", int.class);
			leg = this.repository.findLegById(id);
			if (leg == null)
				manager = null;
			else
				manager = leg.getFlight().getManager();
		} else {
			leg = null;
			manager = null;
		}

		status = leg != null && super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

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
		dataset.put("travelHours", leg.getTravelHours());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("departureAirports", airportDepartureChoices);
		dataset.put("departureAirport", airportDepartureChoices.getSelected().getKey());
		dataset.put("arrivalAirports", airportArrivalChoices);
		dataset.put("arrivalAirport", airportArrivalChoices.getSelected().getKey());

		super.getResponse().addData(dataset);

	}
}
