
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.activityLog.ActivityLog;
import acme.entities.leg.Leg;
import acme.features.flightCrewMember.flightAssignment.FlightAssignmentRepository;

public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void initialize(final ValidActivityLog constraintAnnotation) {
		// Initial setup if needed.
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert activityLog != null;
		boolean result = true;

		Leg leg = this.repository.findLegById(activityLog.getFlightAssignment().getLeg().getId());

		if (activityLog.getTypeOfIncident().isBlank() || activityLog.getTypeOfIncident().isEmpty() || activityLog.getTypeOfIncident().length() > 255) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.activityLog.typeOfIncident.message").addPropertyNode("typeOfIncident").addConstraintViolation();
			result = false;
		} else if (activityLog.getDescription().isBlank() || activityLog.getDescription().isEmpty() || activityLog.getDescription().length() > 255) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.activityLog.description.message").addPropertyNode("description").addConstraintViolation();
			result = false;
		} else if (activityLog.getRegistrationMoment() == null || activityLog.getRegistrationMoment().before(leg.getScheduledDeparture()) || activityLog.getRegistrationMoment().after(leg.getScheduledArrival())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.activityLog.registrationMoment.message").addPropertyNode("registrationMoment").addConstraintViolation();
			result = false;
		} else if (activityLog.getSeverity() == null || activityLog.getSeverity() > 0 || activityLog.getSeverity() < 10) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.activityLog.severity.message").addPropertyNode("severity").addConstraintViolation();
			result = false;
		} else if (activityLog.getFlightAssignment() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.activityLog.flighAssignment.message").addPropertyNode("flightAssignment").addConstraintViolation();
			result = false;
		}

		return result;

	}

}
