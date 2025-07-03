
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineRepository;

public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	@Override
	protected void initialise(final ValidAirline constraintAnnotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		assert airline != null;
		assert context != null;

		boolean result = true;

		// Validar que "name" no sea nulo ni vacío
		if (airline.getName() == null || airline.getName().isEmpty()) {
			super.state(context, false, "name", "acme.validation.Airline.name.message");
			result = false;
		}

		// Validar que "iataCode" no sea nulo y tenga el formato correcto
		if (airline.getIataCode() == null || !airline.getIataCode().matches("^[A-Z]{3}$")) {
			super.state(context, false, "iataCode", "acme.validation.Airline.iataCode.message");
			result = false;
		}

		// Validar que "website" no sea nulo ni vacío
		if (airline.getWebsite() == null || airline.getWebsite().isEmpty()) {
			super.state(context, false, "website", "acme.validation.Airline.website.message");
			result = false;
		}

		// Validar que "type" no sea nulo
		if (airline.getType() == null) {
			super.state(context, false, "type", "acme.validation.Airline.type.message");
			result = false;
		}

		// Validar que "foundationMoment" sea una fecha pasada
		if (airline.getFoundationMoment() == null || airline.getFoundationMoment().after(new java.util.Date())) {
			super.state(context, false, "foundationMoment", "acme.validation.Airline.foundationMoment.message");
			result = false;
		}

		// Validar que "email" tenga un formato adecuado
		if (airline.getEmail() != null && !airline.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			super.state(context, false, "email", "acme.validation.Airline.email.message");
			result = false;
		}

		// Validar que "phoneNumber" tenga un formato adecuado si está presente
		if (airline.getPhoneNumber() != null && !airline.getPhoneNumber().matches("^\\+?\\d{6,15}$")) {
			super.state(context, false, "phoneNumber", "acme.validation.Airline.phoneNumber.message");
			result = false;
		}

		// Validar que la aerolínea exista en la base de datos
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);
		if (airlineRepository != null && !airlineRepository.existsById(airline.getId())) {
			super.state(context, false, "airline", "acme.validation.Airline.airlineNotExist.message");
			result = false;
		}

		return result;
	}
}
