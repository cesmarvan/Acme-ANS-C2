
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.AirlineRepository;

public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Override
	protected void initialise(final ValidAircraft constraintAnnotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert aircraft != null;
		assert context != null;

		boolean result = true;

		// Validar si el "model" no es nulo y tiene un formato adecuado
		if (aircraft.getModel() == null || aircraft.getModel().isEmpty()) {
			super.state(context, false, "model", "acme.validation.Aircraft.model.message");
			result = false;
		}

		// Validar si el "registrationNumber" es único y no nulo
		if (aircraft.getRegistrationNumber() == null || aircraft.getRegistrationNumber().isEmpty()) {
			super.state(context, false, "registrationNumber", "acme.validation.Aircraft.registrationNumber.message");
			result = false;
		}

		// Validar si "capacity" está dentro del rango especificado
		if (aircraft.getCapacity() == null || aircraft.getCapacity() < 0 || aircraft.getCapacity() > 1000) {
			super.state(context, false, "capacity", "acme.validation.Aircraft.capacity.message");
			result = false;
		}

		// Validar si "cargoWeight" está dentro del rango especificado
		if (aircraft.getCargoWeight() == null || aircraft.getCargoWeight() < 2000 || aircraft.getCargoWeight() > 50000) {
			super.state(context, false, "cargoWeight", "acme.validation.Aircraft.cargoWeight.message");
			result = false;
		}

		// Validar si "status" no es nulo
		if (aircraft.getStatus() == null) {
			super.state(context, false, "status", "acme.validation.Aircraft.status.message");
			result = false;
		}

		// Validar si "airline" no es nulo
		if (aircraft.getAirline() == null) {
			super.state(context, false, "airline", "acme.validation.Aircraft.airline.message");
			result = false;
		}

		// Validar que la aerolínea exista en la base de datos
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);
		if (airlineRepository == null || !airlineRepository.existsById(aircraft.getAirline().getId())) {
			super.state(context, false, "airline", "acme.validation.Aircraft.airline.notExist.message");
			result = false;
		}

		return result;
	}
}
