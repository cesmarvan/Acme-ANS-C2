
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;

public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {

	@Override
	protected void initialise(final ValidAirport constraintAnnotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		assert airport != null;
		assert context != null;

		boolean result = true;

		// Validar que "name" no sea nulo ni vacío
		if (airport.getName() == null || airport.getName().isEmpty()) {
			super.state(context, false, "name", "acme.validation.Airport.name.message");
			result = false;
		}

		// Validar que "iataCode" no sea nulo y tenga el formato correcto
		if (airport.getIataCode() == null || !airport.getIataCode().matches("^[A-Z]{3}$")) {
			super.state(context, false, "iataCode", "acme.validation.Airport.iataCode.message");
			result = false;
		}

		// Validar que "operationalScope" no sea nulo
		if (airport.getOperationalScope() == null) {
			super.state(context, false, "operationalScope", "acme.validation.Airport.operationalScope.message");
			result = false;
		}

		// Validar que "city" no sea nulo ni vacío
		if (airport.getCity() == null || airport.getCity().isEmpty()) {
			super.state(context, false, "city", "acme.validation.Airport.city.message");
			result = false;
		}

		// Validar que "country" no sea nulo ni vacío
		if (airport.getCountry() == null || airport.getCountry().isEmpty()) {
			super.state(context, false, "country", "acme.validation.Airport.country.message");
			result = false;
		}

		// Validar que "web" tenga un formato válido si está presente
		if (airport.getWeb() != null && !airport.getWeb().matches("^(http|https)://.*$")) {
			super.state(context, false, "web", "acme.validation.Airport.web.message");
			result = false;
		}

		// Validar que "emailAddress" tenga un formato adecuado si está presente
		if (airport.getEmailAddress() != null && !airport.getEmailAddress().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			super.state(context, false, "emailAddress", "acme.validation.Airport.emailAddress.message");
			result = false;
		}

		// Validar que "contactPhoneNumber" tenga un formato adecuado si está presente
		if (airport.getContactPhoneNumber() != null && !airport.getContactPhoneNumber().matches("^\\+?\\d{10,15}$")) {
			super.state(context, false, "contactPhoneNumber", "acme.validation.Airport.contactPhoneNumber.message");
			result = false;
		}

		// Validar que el aeropuerto exista en la base de datos
		AirportRepository airportRepository = SpringHelper.getBean(AirportRepository.class);
		if (airportRepository != null && !airportRepository.existsById(airport.getId())) {
			super.state(context, false, "airport", "acme.validation.Airport.notExist.message");
			result = false;
		}

		return result;
	}
}
