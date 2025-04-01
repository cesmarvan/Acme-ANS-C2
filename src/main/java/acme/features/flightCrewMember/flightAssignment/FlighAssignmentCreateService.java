
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.crewMember.AvailabilityStatus;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.CrewDuties;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlighAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository flightAssignmentRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMember crewMember;

		crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		flightAssignment = new FlightAssignment();
		flightAssignment.setDraftMode(true);
		flightAssignment.setFlightCrewMember(crewMember);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
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
	public void validate(final FlightAssignment flightAssignment) {
		{

			List<CrewDuties> ls = this.flightAssignmentRepository.findPilotsInLegByLegId(flightAssignment.getLeg().getId());
			boolean status = true;
			if (flightAssignment.getDuty().equals(CrewDuties.PILOT))
				if (ls.contains(CrewDuties.PILOT))
					status = false;
			if (flightAssignment.getDuty().equals(CrewDuties.COPILOT))
				if (ls.contains(CrewDuties.COPILOT))
					status = false;
			super.state(status, "duty", "acme.validation.confirmation.message.moreThanOnePilotOrCopilot");

		}
		{

			boolean crewMemberAvailable;
			crewMemberAvailable = flightAssignment.getFlightCrewMember().getStatus() == AvailabilityStatus.AVAILABLE;
			super.state(crewMemberAvailable, "flightCrewMember", "acme.validation.confirmation.message.crewMemberNotAvailable");

		}

		{
			boolean onlyOneLeg;
			onlyOneLeg = this.flightAssignmentRepository.findLegsByCrewMemberId(flightAssignment.getFlightCrewMember().getId()).isEmpty();
			super.state(onlyOneLeg, "flightCrewMember", "acme.validation.confirmation.message.crewMemberAlreadyInLeg");

		}
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Date lastUpdate = MomentHelper.getCurrentMoment();
		flightAssignment.setLastUpdate(lastUpdate);
		this.flightAssignmentRepository.save(flightAssignment);
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

		List<Leg> legList = this.flightAssignmentRepository.findAllLegs();
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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
