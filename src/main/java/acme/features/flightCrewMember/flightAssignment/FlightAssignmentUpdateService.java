
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.crewMember.AvailabilityStatus;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.CrewDuties;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository flightAssignmentRepository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		FlightAssignment flightAssignment;
		FlightCrewMember crewMember;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.flightAssignmentRepository.findFlightAssignmentById(flightAssignmentId);
		crewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();

		status = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(crewMember);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		FlightAssignment flightAssignment;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.flightAssignmentRepository.findFlightAssignmentById(id);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		int crewMemberId = super.getRequest().getData("flightCrewMember", int.class);
		FlightCrewMember crewMember = this.flightAssignmentRepository.findCrewMemberById(crewMemberId);

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.flightAssignmentRepository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "status", "remarks");

		flightAssignment.setFlightCrewMember(crewMember);
		flightAssignment.setLeg(leg);

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		this.flightAssignmentRepository.save(flightAssignment);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		{
			if (flightAssignment.getLeg() != null) {
				List<CrewDuties> ls = this.flightAssignmentRepository.findPilotsInLegByLegId(flightAssignment.getLeg().getId());
				boolean status = true;
				if (flightAssignment.getDuty().equals(CrewDuties.PILOT))
					if (ls.contains(CrewDuties.PILOT))
						status = false;
				if (flightAssignment.getDuty().equals(CrewDuties.COPILOT))
					if (ls.contains(CrewDuties.COPILOT))
						status = false;
				super.state(status, "duty", "validation.error.messagemoreThanOnePilotOrCopilot");
			}
		}
		{

			boolean crewMemberAvailable;
			crewMemberAvailable = flightAssignment.getFlightCrewMember().getStatus() == AvailabilityStatus.AVAILABLE;
			super.state(crewMemberAvailable, "flightCrewMember", "validation.error.messagecrewMemberNotAvailable");

		}
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		SelectChoices crewMemberChoices;

		dutyChoices = SelectChoices.from(CrewDuties.class, null);
		statusChoices = SelectChoices.from(AssignmentStatus.class, null);

		List<Leg> legList = this.flightAssignmentRepository.findAllPublishedLegs();
		legChoices = SelectChoices.from(legList, "flightNumber", null);

		List<FlightCrewMember> crewMemberList = this.flightAssignmentRepository.findAllCrewMembers();
		crewMemberChoices = SelectChoices.from(crewMemberList, "employeeCode", null);

		dataset = super.unbindObject(flightAssignment, "duty", "status", "lastUpdate", "draftMode");
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("lastUpdate", flightAssignment.getLastUpdate());
		dataset.put("flightCrewMembers", crewMemberChoices);
		dataset.put("flightCrewMember", crewMemberChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
