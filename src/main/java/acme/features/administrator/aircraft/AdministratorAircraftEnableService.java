
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAircraftEnableService extends AbstractGuiService<Administrator, Aircraft> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAdministrator = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(isAdministrator);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {

		super.bindObject(aircraft, "airline", "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

	}

	@Override
	public void perform(final Aircraft aircraft) {

		Aircraft a = this.repository.findAircraftById(aircraft.getId());

		System.out.println("Current status: " + a.getStatus());

		if (a.getStatus() == AircraftStatus.UNDER_MAINTENANCE) {
			a.setStatus(AircraftStatus.ACTIVE_SERVICE);
			System.out.println("Status changed to: " + a.getStatus());
		}

		a.setModel(aircraft.getModel());
		a.setRegistrationNumber(aircraft.getRegistrationNumber());
		a.setCapacity(aircraft.getCapacity());
		a.setCargoWeight(aircraft.getCargoWeight());
		a.setDetails(aircraft.getDetails());
		a.setAirline(aircraft.getAirline());

		this.repository.save(a);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices airlineChoices;

		Collection<Airline> airlines = this.repository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "id", aircraft.getAirline());
		choices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset = super.unbindObject(aircraft, "airline", "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("aircraftStatus", choices);
		dataset.put("confirmation", false);
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
