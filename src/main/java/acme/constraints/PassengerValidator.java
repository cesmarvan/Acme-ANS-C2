
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.passenger.Passenger;

public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	@Override
	protected void initialise(final ValidPassenger annotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		assert passenger != null;
		assert context != null;

		boolean result = true;

		// Validar que "fullName" no sea nulo y que cumpla con los criterios del "ValidLongText"
		if (passenger.getFullName() == null || passenger.getFullName().length() < 1) {
			super.state(context, false, "fullName", "acme.validation.Passenger.fullName.message");
			result = false;
		}

		// Validar que "email" no sea nulo y que cumpla con el formato del "ValidEmail"
		if (passenger.getEmail() == null || !passenger.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			super.state(context, false, "email", "acme.validation.Passenger.email.message");
			result = false;
		}

		// Validar que "passportNumber" sea válido según el patrón "^[A-Z0-9]{6,9}$"
		if (passenger.getPassportNumber() == null || !passenger.getPassportNumber().matches("^[A-Z0-9]{6,9}$")) {
			super.state(context, false, "passportNumber", "acme.validation.Passenger.passportNumber.message");
			result = false;
		}

		// Validar que "dateOfBirth" sea una fecha pasada
		if (passenger.getDateOfBirth() == null || passenger.getDateOfBirth().after(new java.util.Date())) {
			super.state(context, false, "dateOfBirth", "acme.validation.Passenger.dateOfBirth.message");
			result = false;
		}

		// Validar que "specialNeeds" no exceda los límites de "ValidShortText"
		if (passenger.getSpecialNeeds() != null && passenger.getSpecialNeeds().length() > 255) {
			super.state(context, false, "specialNeeds", "acme.validation.Passenger.specialNeeds.message");
			result = false;
		}

		// Validar que "customer" no sea nulo
		if (passenger.getCustomer() == null) {
			super.state(context, false, "customer", "acme.validation.Passenger.customer.message");
			result = false;
		}

		result = !super.hasErrors(context);

		return result;
	}
}
