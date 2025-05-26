
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
public class ActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		int activityLogId;
		ActivityLog activityLog;
		FlightCrewMember crewMember;
		try {
			activityLogId = super.getRequest().getData("id", int.class);
			activityLog = this.repository.findActivityLogById(activityLogId);
			crewMember = activityLog == null ? null : activityLog.getFlightAssignment().getFlightCrewMember();

			status = activityLog != null && super.getRequest().getPrincipal().hasRealm(crewMember) && crewMember.getId() == super.getRequest().getPrincipal().getActiveRealm().getId() && activityLog.getDraftMode();
		} catch (Exception e) {
			status = false;
		}
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
		int flightAssignmentId = super.getRequest().getData("flightAssignment", int.class);
		FlightAssignment fa = this.repository.findFlightAssignmentById(flightAssignmentId);

		activityLog.setFlightAssignment(fa);

		super.bindObject(activityLog, "registrationMoment", "description", "severity", "typeOfIncident");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		boolean status;
		FlightAssignment flightAssignment = activityLog.getFlightAssignment();
		if (flightAssignment != null) {
			status = flightAssignment.getDraftMode();
			super.state(!status, "flightAssignment", "validation.error.flightAssignmentNotPublished");
		}
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentByCrewMemberId(crewMemberId);
		SelectChoices assignmentChoices = SelectChoices.from(flightAssignments, "leg.flightNumber", activityLog.getFlightAssignment());

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity", "draftMode");

		dataset.put("assignmentChoices", assignmentChoices);
		dataset.put("flightAssignment", assignmentChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
