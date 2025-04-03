
package acme.features.flightCrewMember.ActivityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class ActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		FlightCrewMember crewMember;

		crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentByCrewMemberId(crewMember.getId());
		activityLog = new ActivityLog();
		activityLog.setDraftMode(true);
		activityLog.setFlightAssignment(flightAssignment);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		int flightAssignmentId = super.getRequest().getData("flightAssignment", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity");
		activityLog.setFlightAssignment(flightAssignment);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		FlightAssignment flightAssignment = this.repository.findFlightAssignmentByCrewMemberId(activityLog.getFlightAssignment().getFlightCrewMember().getId());

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity", "draftMode");

		dataset.put("flightAssignment", flightAssignment.getId());

		super.getResponse().addData(dataset);
	}

}
