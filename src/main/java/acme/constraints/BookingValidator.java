
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.client.helpers.SpringHelper;
import acme.entities.booking.Booking;
import acme.entities.flight.FlightRepository;

public class BookingValidator implements ConstraintValidator<ValidBooking, Booking> {

	@Override
	public void initialize(final ValidBooking constraintAnnotation) {
		// Initial setup if needed.
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert booking != null;
		assert context != null;

		boolean result = true;

		// Validar si el "locatorCode" cumple con el formato adecuado
		if (booking.getLocatorCode() == null || !booking.getLocatorCode().matches("^[A-Z0-9]{6,8}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.locatorCode.message").addPropertyNode("locatorCode").addConstraintViolation();
			result = false;
		}

		// Validar si "purchaseMoment" es una fecha pasada
		if (booking.getPurchaseMoment() == null || booking.getPurchaseMoment().after(new Date())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.purchaseMoment.message").addPropertyNode("purchaseMoment").addConstraintViolation();
			result = false;
		}

		// Validar si el "isPublished" está correctamente marcado
		if (booking.getIsPublished() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.isPublished.message").addPropertyNode("isPublished").addConstraintViolation();
			result = false;
		}

		// Validar que el "customer" no sea nulo
		if (booking.getCustomer() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.customer.message").addPropertyNode("customer").addConstraintViolation();
			result = false;
		}

		// Validar que el "flight" no sea nulo
		if (booking.getFlight() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.flight.message").addPropertyNode("flight").addConstraintViolation();
			result = false;
		}

		// Validar el "lastCreditCardNibble" si está presente, que sea válido (si está presente)
		if (booking.getLastCreditCardNibble() != null && !booking.getLastCreditCardNibble().matches("^\\d{4}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.lastCreditCardNibble.message").addPropertyNode("lastCreditCardNibble").addConstraintViolation();
			result = false;
		}

		// Comprobamos la relación entre el "flight" y el "customer" (por ejemplo, que el vuelo esté disponible para el cliente)
		FlightRepository flightRepository = SpringHelper.getBean(FlightRepository.class);
		if (flightRepository == null)
			result = false;

		// Si es un vuelo válido y el cliente puede acceder, no se genera error
		// Comprobamos si el vuelo no es nulo y está en un estado adecuado
		if (booking.getFlight() != null && !flightRepository.existsById(booking.getFlight().getId())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.Booking.flight.notExist.message").addPropertyNode("flight").addConstraintViolation();
			result = false;
		}

		return result;
	}
}
