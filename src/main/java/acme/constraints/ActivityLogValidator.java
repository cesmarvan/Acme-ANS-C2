
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
		Leg leg = null;
		if (activityLog.getFlightAssignment() != null)
			leg = this.repository.findLegById(activityLog.getFlightAssignment().getLeg().getId());

		if (activityLog.getTypeOfIncident().isBlank() || activityLog.getTypeOfIncident().isEmpty() || activityLog.getTypeOfIncident().length() > 255) {
			super.state(context, false, "typeOfIncident", "acme.validation.activityLog.typeOfIncident.message");
			result = false;
		} else if (activityLog.getDescription().isBlank() || activityLog.getDescription().isEmpty() || activityLog.getDescription().length() > 255) {
			super.state(context, false, "description", "acme.validation.activityLog.description.message");
			result = false;
		} else if (leg != null && (activityLog.getRegistrationMoment() == null || activityLog.getRegistrationMoment().before(leg.getScheduledDeparture()) || activityLog.getRegistrationMoment().after(leg.getScheduledArrival()))) {
			super.state(context, false, "registrationMoment", "acme.validation.activityLog.registrationMoment.message");
			result = false;
		} else if (activityLog.getSeverity() == null || activityLog.getSeverity() < 0 || activityLog.getSeverity() > 10) {
			super.state(context, false, "severity", "acme.validation.activityLog.severity.message");
			result = false;
		} else if (activityLog.getFlightAssignment() == null) {
			super.state(context, false, "flightAssignment", "acme.validation.activityLog.flightAssignment.message");
			result = false;
		}
		return result;

	}

}
