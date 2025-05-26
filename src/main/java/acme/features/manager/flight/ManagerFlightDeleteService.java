
package acme.features.manager.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {

	// Internal state --------------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface ------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		if (super.getRequest().hasData("id", int.class)) {
			flightId = super.getRequest().getData("id", int.class);
			flight = this.repository.findFlightById(flightId);
			if (flight == null)
				manager = null;
			else
				manager = flight.getManager();
		}

		else {
			manager = null;
			flight = null;
		}

		status = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		{
			boolean statusLegsPublished = true;

			List<Leg> flightLegs = this.repository.getLegsOfFlight(flight.getId());

			for (Leg l : flightLegs)
				if (Boolean.FALSE.equals(l.getDraftMode())) {
					statusLegsPublished = false;
					break;
				}

			super.state(statusLegsPublished, "*", "acme.validation.flight.published-legs.message");
		}
	}

	@Override
	public void perform(final Flight flight) {
		List<Leg> legs = this.repository.getLegsOfFlight(flight.getId());

		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("scheduleDeparture", flight.getScheduleDeparture());
		dataset.put("scheduleArrival", flight.getScheduleArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("layovers", flight.getNumberLavoyers());

		super.getResponse().addData(dataset);
	}
}
