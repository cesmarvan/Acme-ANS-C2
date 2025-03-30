
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.CrewDuties;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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

		crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightAssignments = this.flightAssignmentRepository.findFlightAssignmentByCrewMemberId(crewMemberId);
		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		SelectChoices crewMemberChoices;
		SelectChoices legChoices;

		List<Leg> legList = this.flightAssignmentRepository.findAllLegs();
		List<FlightCrewMember> crewMemberList = this.flightAssignmentRepository.findAllCrewMembers();

		dutyChoices = SelectChoices.from(CrewDuties.class, flightAssignment.getDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());

		crewMemberChoices = SelectChoices.from(crewMemberList, "employeeCode", flightAssignment.getFlightCrewMember());
		legChoices = SelectChoices.from(legList, "flightNumber", flightAssignment.getLeg());

		dataset = super.unbindObject(flightAssignment, "duty", "status", "remarks", "lastUpdate");
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("lastUpdate", flightAssignment.getLastUpdate());
		dataset.put("remarks", flightAssignment.getRemarks());
		dataset.put("flightCrewMembers", crewMemberChoices);
		dataset.put("flightCrewMember", crewMemberChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
