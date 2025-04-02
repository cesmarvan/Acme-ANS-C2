
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	// Internal state ----------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		Manager manager;
		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Leg> legs;
		int managerId;
		int masterId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		masterId = super.getRequest().getData("masterId", int.class);
		legs = this.repository.findAllLegsByFlightAndManagerId(masterId, managerId);
		super.getBuffer().addData(legs);
		super.getResponse().addGlobal("flightId", masterId);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;

		dataset = super.unbindObject(leg, "id", "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		super.getResponse().addData(dataset);

	}

}
