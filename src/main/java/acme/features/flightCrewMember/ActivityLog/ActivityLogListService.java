
package acme.features.flightCrewMember.ActivityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiService
public class ActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int crewMemberId;
		List<ActivityLog> activityLogs;

		crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		activityLogs = this.repository.findActivityLogsByFlightCrewMemberId(crewMemberId);
		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severity", "flightAssignment.id");

		super.getResponse().addData(dataset);
	}

}
