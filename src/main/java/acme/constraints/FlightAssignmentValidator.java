
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightAssignment.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightAssignmentRepository;

public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result = true;

		if (flightAssignment.getDuty() == null) {
			super.state(context, false, "duty", "acme.validation.flightAssignment.duty.message");
			result = false;
		} else if (flightAssignment.getLastUpdate() == null || flightAssignment.getLastUpdate().after(MomentHelper.getCurrentMoment())) {
			super.state(context, false, "lastUpdate", "acme.validation.flightAssignment.lastUpdate.message");
			result = false;
		} else if (flightAssignment.getFlightCrewMember() == null) {
			super.state(context, false, "flightCrewMember", "acme.validation.flightAssignment.crewMember.message");
			result = false;
		} else if (!flightAssignment.getRemarks().isBlank() && (flightAssignment.getRemarks().length() > 255 || flightAssignment.getRemarks().length() < 0)) {
			super.state(context, false, "remarks", "acme.validation.flightAssignment.remarks.message");
			result = false;
		} else if (flightAssignment.getLeg() == null) {
			super.state(context, false, "leg", "acme.validation.flightAssignment.leg.message");
			result = false;
		} else if (flightAssignment.getStatus() == null) {
			super.state(context, false, "status", "acme.validation.flightAssignment.status.message");
			result = false;
		}

		return result;
	}

}
