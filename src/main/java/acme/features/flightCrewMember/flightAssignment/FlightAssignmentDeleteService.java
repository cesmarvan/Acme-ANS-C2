//

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
public class FlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository flightAssignmentRepository;


	@Override
	public void authorise() {
		boolean status = true;
		int flightAssignmentId;
		FlightAssignment flightAssignment;
		FlightCrewMember crewMember;
		String method = super.getRequest().getMethod();
		try {
			if (method.equals("POST")) {
				flightAssignmentId = super.getRequest().getData("id", int.class);
				flightAssignment = this.flightAssignmentRepository.findFlightAssignmentById(flightAssignmentId);
				crewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();

				status = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(crewMember) && flightAssignment.getDraftMode();
			}
		} catch (Exception e) {
			status = false;
		}
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
		FlightCrewMember crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.flightAssignmentRepository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "status", "remarks");

		flightAssignment.setFlightCrewMember(crewMember);
		flightAssignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.flightAssignmentRepository.delete(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		SelectChoices legChoices;

		List<Leg> legList = this.flightAssignmentRepository.findAllPublishedLegs();
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

}
