
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
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.duty.message").addPropertyNode("duty").addConstraintViolation();
			result = false;
		} else if (flightAssignment.getLastUpdate() == null || flightAssignment.getLastUpdate().after(MomentHelper.getCurrentMoment())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.lastUpdate.message").addPropertyNode("lastUpdate").addConstraintViolation();
			result = false;
		} else if (flightAssignment.getFlightCrewMember() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.flightCrewMember.message").addPropertyNode("flightCrewMember").addConstraintViolation();
			result = false;
		} else if (flightAssignment.getRemarks().isBlank() || flightAssignment.getRemarks().isEmpty() || flightAssignment.getRemarks().length() > 255 || flightAssignment.getRemarks().length() < 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.remarks.message").addPropertyNode("remarks").addConstraintViolation();
			result = false;
		}

		else if (flightAssignment.getLeg() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.leg.message").addPropertyNode("leg").addConstraintViolation();
			result = false;
		} else if (flightAssignment.getStatus() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.status.message").addPropertyNode("status").addConstraintViolation();
			result = false;
		}

		return result;
	}

}
