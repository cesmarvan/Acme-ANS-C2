
package acme.features.crewMember;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMember crewMember;

		crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(crewMember);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		int crewMemberId = super.getRequest().getData("id", int.class);
		FlightCrewMember crewMember = this.flightAssignmentRepository.findCrewMemberById(crewMemberId);

		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.flightAssignmentRepository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "lastUpdate", "status", "remarks");

		flightAssignment.setFlightCrewMember(crewMember);
		flightAssignment.setLeg(leg);

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.flightAssignmentRepository.save(flightAssignment);
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
