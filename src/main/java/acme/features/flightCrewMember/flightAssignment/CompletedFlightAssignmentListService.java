
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class CompletedFlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository flightAssignmentRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int crewMemberId;
		List<FlightAssignment> flightAssignments;
		Date now = MomentHelper.getCurrentMoment();

		crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightAssignments = this.flightAssignmentRepository.findFlightAssignmentCompletedLeg(crewMemberId, now);
		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "status", "remarks", "lastUpdate", "leg.flightNumber", "flightCrewMember.employeeCode", "leg.scheduledDeparture");

		super.getResponse().addData(dataset);
	}
}
