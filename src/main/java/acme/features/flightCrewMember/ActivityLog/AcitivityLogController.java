
package acme.features.flightCrewMember.ActivityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiController
public class AcitivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogCreateService	createService;

	@Autowired
	private ActivityLogShowService		showService;

	@Autowired
	private ActivityLogListService		listService;

	@Autowired
	private ActivityLogUpdateService	updateService;

	@Autowired
	private ActivityLogPublishService	publishService;

	@Autowired
	private ActivityLogDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
