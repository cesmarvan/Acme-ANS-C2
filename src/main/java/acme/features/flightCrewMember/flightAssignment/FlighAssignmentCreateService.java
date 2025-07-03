
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
		boolean status = true;
		String method = super.getRequest().getMethod();
		try {
			status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
			if (method.equals("POST")) {
				int legId = super.getRequest().getData("leg", int.class);
				String legIdStr = super.getRequest().getData("leg", String.class);
				Leg assignmentLeg = this.flightAssignmentRepository.findLegById(legId);
				List<Leg> publishedLegs = this.flightAssignmentRepository.findAllUpcomingPublishedLegs(MomentHelper.getCurrentMoment());
				if (!"0".equals(legIdStr) && (assignmentLeg == null || !publishedLegs.contains(assignmentLeg)))
					status = false;
			}
		} catch (Exception e) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
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

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.flightAssignmentRepository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "status", "remarks");

		flightAssignment.setLeg(leg);

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		{
			if (flightAssignment.getLeg() != null && flightAssignment.getDuty() != null) {
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
			if (flightAssignment.getFlightCrewMember() != null) {
				boolean crewMemberAvailable;
				crewMemberAvailable = flightAssignment.getFlightCrewMember().getStatus() == AvailabilityStatus.AVAILABLE;
				super.state(crewMemberAvailable, "*", "validation.error.messagecrewMemberNotAvailable");
			}
		}

		{
			if (flightAssignment.getFlightCrewMember() != null && flightAssignment.getLeg() != null) {
				boolean onlyOneLeg;
				onlyOneLeg = this.flightAssignmentRepository
					.findSimultaneousLegsByMember(flightAssignment.getLeg().getScheduledDeparture(), flightAssignment.getLeg().getScheduledArrival(), flightAssignment.getLeg().getId(), flightAssignment.getFlightCrewMember().getId()).isEmpty();
				super.state(onlyOneLeg, "*", "validation.error.messagecrewMemberAlreadyInLeg");
			}
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
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		SelectChoices legChoices;

		List<Leg> legList = this.flightAssignmentRepository.findAllUpcomingPublishedLegs(MomentHelper.getCurrentMoment());
		legChoices = SelectChoices.from(legList, "flightNumber", flightAssignment.getLeg());

		dutyChoices = SelectChoices.from(CrewDuties.class, flightAssignment.getDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());

		dataset = super.unbindObject(flightAssignment, "duty", "remarks", "status", "lastUpdate", "draftMode");
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("lastUpdate", flightAssignment.getLastUpdate());
		dataset.put("remarks", flightAssignment.getRemarks());
		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember());
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
