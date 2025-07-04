
package acme.features.manager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiController
public class ManagerLegController extends AbstractGuiController<Manager, Leg> {

	// Internal state ----------------------------------------------------

	@Autowired
	private ManagerLegListService	listService;

	@Autowired
	private ManagerLegShowService	showService;

	@Autowired
	private ManagerLegCreateService	createService;

	@Autowired
	ManagerLegDeleteService			deleteService;

	@Autowired
	ManagerLegUpdateService			updateService;

	@Autowired
	ManagerLegPublishService		publishService;

	// Constructors --------------------------------------------------------


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
