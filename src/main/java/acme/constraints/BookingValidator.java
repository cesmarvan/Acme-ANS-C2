
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.booking.Booking;
import acme.entities.flight.FlightRepository;

public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Override
	protected void initialise(final ValidBooking annotation) {
		// Initial setup if needed.
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert booking != null;
		assert context != null;

		boolean result = true;

		// Validar si el "locatorCode" cumple con el formato adecuado
		if (booking.getLocatorCode() == null || !booking.getLocatorCode().matches("^[A-Z0-9]{6,8}$")) {
			super.state(context, false, "locatorCode", "acme.validation.Booking.locatorCode.message");
			result = false;
		}

		// Validar si "purchaseMoment" es una fecha pasada
		if (booking.getPurchaseMoment() == null || booking.getPurchaseMoment().after(new java.util.Date())) {
			super.state(context, false, "purchaseMoment", "acme.validation.Booking.purchaseMoment.message");
			result = false;
		}

		// Validar si el "isPublished" está correctamente marcado
		if (booking.getIsPublished() == null) {
			super.state(context, false, "isPublished", "acme.validation.Booking.isPublished.message");
			result = false;
		}

		// Validar que el "customer" no sea nulo
		if (booking.getCustomer() == null) {
			super.state(context, false, "customer", "acme.validation.Booking.customer.message");
			result = false;
		}

		// Validar que el "flight" no sea nulo
		if (booking.getFlight() == null) {
			super.state(context, false, "flight", "acme.validation.Booking.flight.message");
			result = false;
		}

		// Validar el "lastCreditCardNibble" si está presente, que sea válido (si está presente)
		if (booking.getLastCreditCardNibble() != null && !booking.getLastCreditCardNibble().matches("^\\d{4}$")) {
			super.state(context, false, "lastCreditCardNibble", "acme.validation.Booking.lastCreditCardNibble.message");
			result = false;
		}

		// Comprobamos la relación entre el "flight" y el "customer" (por ejemplo, que el vuelo esté disponible para el cliente)
		FlightRepository flightRepository = SpringHelper.getBean(FlightRepository.class);
		if (flightRepository == null) {
			super.state(context, false, "flight", "acme.validation.Booking.flight.notExist.message");
			result = false;
		}

		// Si es un vuelo válido y el cliente puede acceder, no se genera error
		if (booking.getFlight() != null && !flightRepository.existsById(booking.getFlight().getId())) {
			super.state(context, false, "flight", "acme.validation.Booking.flight.notExist.message");
			result = false;
		}

		return result;
	}
}
