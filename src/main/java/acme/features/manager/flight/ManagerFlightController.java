
package acme.features.manager.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiController
public class ManagerFlightController extends AbstractGuiController<Manager, Flight> {

	// Internal state ------------------------------------------------------------------------------

	@Autowired
	private ManagerFlightListService	listService;

	@Autowired
	private ManagerFlightShowService	showService;

	@Autowired
	private ManagerFlightCreateService	createService;

	@Autowired
	private ManagerFlightDeleteService	deleteService;

	@Autowired
	private ManagerFlightUpdateService	updateService;

	@Autowired
	private ManagerFlightPublishService	publishService;

	// Constructors --------------------------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
