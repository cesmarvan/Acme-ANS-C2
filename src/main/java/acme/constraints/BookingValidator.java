
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.booking.Booking;
import acme.features.customer.booking.CustomerBookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	protected void initialise(final ValidBooking constraintAnnotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert booking != null;
		assert context != null;

		boolean result = true;

		// Validar que "locatorCode" sea válido con el patrón "^[A-Z0-9]{6,8}$ o ya exista"
		if (booking.getLocatorCode() == null || !booking.getLocatorCode().matches("^[A-Z0-9]{6,8}$"))
			super.state(context, false, "locatorCode", "acme.validation.Booking.locatorCode.message");
		else {
			Booking b = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
			if (b != null && b.getId() != booking.getId())
				super.state(context, false, "locatorCode", "acme.validation.Booking.locatorCode.exists.message");

		}

		// Validar que "purchaseMoment" sea una fecha pasada
		if (booking.getPurchaseMoment() == null || booking.getPurchaseMoment().after(MomentHelper.getCurrentMoment())) {
			super.state(context, false, "purchaseMoment", "acme.validation.Booking.purchaseMoment.message");
			result = false;
		}

		// Validar que "isPublished" no sea nulo
		if (booking.getIsPublished() == null) {
			super.state(context, false, "isPublished", "acme.validation.Booking.isPublished.message");
			result = false;
		}

		// Validar que "customer" no sea nulo
		if (booking.getCustomer() == null) {
			super.state(context, false, "customer", "acme.validation.Booking.customer.message");
			result = false;
		}

		// Validar que "flight" no sea nulo
		if (booking.getFlight() == null) {
			super.state(context, false, "flight", "acme.validation.Booking.flight.message");
			result = false;
		}

		// Validar que "lastCreditCardNibble" sea válido si está presente
		if (booking.getLastCreditCardNibble() != null && !booking.getLastCreditCardNibble().matches("^\\d{4}$")) {
			super.state(context, false, "lastCreditCardNibble", "acme.validation.Booking.lastCreditCardNibble.message");
			result = false;
		}

		return result;
	}
}
