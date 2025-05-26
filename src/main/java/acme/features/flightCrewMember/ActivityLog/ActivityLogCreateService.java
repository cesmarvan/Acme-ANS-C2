
package acme.features.flightCrewMember.ActivityLog;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		boolean authorised = true;

		try {
			String method = super.getRequest().getMethod();
			if (method.equals("POST")) {
				Date currentMoment = MomentHelper.getCurrentMoment();
				int flightCrewMemberID = super.getRequest().getPrincipal().getActiveRealm().getId();
				int flightAssignmentId = super.getRequest().getData("flightAssignment", int.class);
				FlightAssignment fa = this.repository.findFlightAssignmentById(flightAssignmentId);
				List<FlightAssignment> possibleAssignments = this.repository.findAssignmentsByMemberIdCompletedLegs(currentMoment, flightCrewMemberID);
				String rawAssignment = super.getRequest().getData("flightAssignment", String.class);

				if (!"0".equals(rawAssignment))
					if (fa == null || !possibleAssignments.contains(fa))
						authorised = false;
			}
		} catch (Exception e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);

		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		ActivityLog activityLog;
		activityLog = new ActivityLog();
		activityLog.setDraftMode(true);
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

		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentByCrewMemberId(crewMemberId);
		SelectChoices assignmentChoices = SelectChoices.from(flightAssignments, "leg.flightNumber", activityLog.getFlightAssignment());

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity", "draftMode");

		dataset.put("assignmentChoices", assignmentChoices);
		dataset.put("flightAssignment", assignmentChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
