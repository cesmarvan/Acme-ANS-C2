
package acme.features.flightCrewMember.ActivityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class ActivityLogUpdateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int activityLogId;
		ActivityLog activityLog;
		FlightCrewMember crewMember;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		crewMember = activityLog == null ? null : activityLog.getFlightAssignment().getFlightCrewMember();

		status = activityLog != null && super.getRequest().getPrincipal().hasRealm(crewMember);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		ActivityLog activityLog;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		int crewMemberId = super.getRequest().getData("flightCrewMember", int.class);
		FlightCrewMember crewMember = this.repository.findCrewMemberById(crewMemberId);

		int flightAssignmentId = super.getRequest().getData("flightAssignment", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity");

		activityLog.setFlightAssignment(flightAssignment);
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		SelectChoices flightAssignmentChoices;

		List<FlightAssignment> flightAssignmentList = this.repository.findAllFlightAssignments();
		flightAssignmentChoices = SelectChoices.from(flightAssignmentList, "id", null);

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity", "draftMode");

		dataset.put("flightAssignments", flightAssignmentChoices);
		dataset.put("flightAssignment", flightAssignmentChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
