
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		if (flightAssignment == null)
			return;

		crewMember = flightAssignment.getFlightCrewMember();

		status = super.getRequest().getPrincipal().hasRealm(crewMember) && flightAssignment.getDraftMode();
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

		super.bindObject(flightAssignment, "duty", "status", "flightCrewMember", "leg");

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdate", "status", "remarks", "flightCrewMember", "leg");
		dataset.put("duty", flightAssignment.getDuty());
		dataset.put("lastUpdate", flightAssignment.getLastUpdate());
		dataset.put("status", flightAssignment.getStatus());
		dataset.put("remarks", flightAssignment.getRemarks());
		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember());
		dataset.put("leg", flightAssignment.getLeg());
	}
}
