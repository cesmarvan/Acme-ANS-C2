
package acme.features.authenticated.crew_member;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.crewMember.FlightCrewMember;
import acme.entities.flightAssignment.FlightAssignment;

@GuiController
public class FlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlighAssignmentCreateService	createService;

	@Autowired
	private FlightAssignmentShowService		showService;

	@Autowired
	private FlightAssignmentListService		listService;

	@Autowired
	private FlightAssignmentUpdateService	updateService;

	@Autowired
	private FlightAssignmentPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);

		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("create", this.createService);

		super.addBasicCommand("update", this.updateService);
	}
}
