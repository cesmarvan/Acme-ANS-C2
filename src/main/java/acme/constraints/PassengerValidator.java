
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.entities.passenger.Passenger;

public class PassengerValidator implements ConstraintValidator<ValidPassenger, Passenger> {

	@Override
	public void initialize(final ValidPassenger constraintAnnotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		assert passenger != null;
		assert context != null;

		boolean result = true;

		// Validar que "fullName" no sea nulo y que cumpla con los criterios del "ValidLongText"
		if (passenger.getFullName() == null || passenger.getFullName().length() < 1) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Passenger.fullName.message").addPropertyNode("fullName").addConstraintViolation();
			result = false;
		}

		// Validar que "email" no sea nulo y que cumpla con el formato del "ValidEmail"
		if (passenger.getEmail() == null || !passenger.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Passenger.email.message").addPropertyNode("email").addConstraintViolation();
			result = false;
		}

		// Validar que "passportNumber" sea válido según el patrón "^[A-Z0-9]{6,9}$"
		if (passenger.getPassportNumber() == null || !passenger.getPassportNumber().matches("^[A-Z0-9]{6,9}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Passenger.passportNumber.message").addPropertyNode("passportNumber").addConstraintViolation();
			result = false;
		}

		// Validar que "dateOfBirth" sea una fecha pasada
		if (passenger.getDateOfBirth() == null || passenger.getDateOfBirth().after(new java.util.Date())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Passenger.dateOfBirth.message").addPropertyNode("dateOfBirth").addConstraintViolation();
			result = false;
		}

		// Validar que "specialNeeds" no exceda los límites de "ValidShortText"
		if (passenger.getSpecialNeeds() != null && passenger.getSpecialNeeds().length() > 255) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Passenger.specialNeeds.message").addPropertyNode("specialNeeds").addConstraintViolation();
			result = false;
		}

		// Validar que "customer" no sea nulo
		if (passenger.getCustomer() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Passenger.customer.message").addPropertyNode("customer").addConstraintViolation();
			result = false;
		}

		return result;
	}
}
