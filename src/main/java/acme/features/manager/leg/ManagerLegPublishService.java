
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiInterface ----------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Manager manager;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);

		if (leg == null)
			manager = null;
		else
			manager = leg.getFlight().getManager();

		status = leg != null && leg.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
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

		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
	}

	@Override
	public void validate(final Leg leg) {
		{
			boolean correctDates = true;

			Flight flight;
			flight = leg.getFlight();

			List<Leg> flightLegs = this.repository.getFisrtLegOfFlight(flight.getId()); // Leg por orden de fecha de salida

			for (int i = 0; i < flightLegs.size(); i++)
				try {
					if (flightLegs.get(i).getScheduledArrival().after(flightLegs.get(i + 1).getScheduledDeparture())) {
						correctDates = false;
						break;
					}
				} catch (Exception e) {
					;
				}

			super.state(correctDates, "*", "acme.validation.flight.no-correct-dates.message");
		}
		{
			boolean correctAirport = true;
			Flight flight;
			flight = leg.getFlight();

			List<Leg> flightLegs = this.repository.getFisrtLegOfFlight(flight.getId()); // Leg por orden de fecha de salida

			for (int i = 0; i < flightLegs.size(); i++)
				try {
					if (!flightLegs.get(i).getArrivalAirport().equals(flightLegs.get(i + 1).getDepartureAirport())) {
						correctAirport = false;
						break;
					}
				} catch (Exception e) {
					continue;
				}

			super.state(correctAirport, "*", "acme.validation.leg.no-correct-airports.message");
		}
		{
			boolean aircraftNotNull = true;

			aircraftNotNull = leg.getAircraft() != null;

			super.state(aircraftNotNull, "aircraft", "acme.validation.leg.null-aircraft.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		Flight flight = leg.getFlight();

		SelectChoices statusChoices;
		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		SelectChoices aircraftChoices;
		List<Aircraft> aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());

		SelectChoices airportDepartureChoices;
		SelectChoices airportArrivalChoices;
		List<Airport> airports = this.repository.findAllAirports();
		airportDepartureChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		airportArrivalChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		dataset.put("duration", leg.getTravelHours());
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
