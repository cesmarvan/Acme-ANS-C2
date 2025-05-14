
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flight.Flight;
import acme.features.manager.flight.ManagerFlightRepository;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state -----------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// ConstraintValidator interface ---------------------------------------------


	@Override
	public boolean isValid(final Flight value, final ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		assert context != null;

		boolean result;

		if (value == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean statusNumberLegs;

			int numberFlightLeg = this.repository.getNumberOfLegsOfFlight(value.getId());

			if (Boolean.TRUE.equals(value.getDraftMode()))
				statusNumberLegs = true;
			else
				statusNumberLegs = numberFlightLeg > 0;

			super.state(context, statusNumberLegs, "tag", "acme.validation.flight.any-legs.message");
		}
		result = !super.hasErrors(context);

		return result;
	}

	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

}
