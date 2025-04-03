
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.service.Service;

public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Override
	protected void initialise(final ValidService constraintAnnotation) {
		// Inicialización si es necesario.
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert service != null;
		assert context != null;

		boolean result = true;

		// Validar que "name" no sea nulo ni vacío
		if (service.getName() == null || service.getName().isEmpty()) {
			super.state(context, false, "name", "acme.validation.Service.name.message");
			result = false;
		}

		// Validar que "pictureLink" no sea nulo y sea una URL válida
		if (service.getPictureLink() == null || !service.getPictureLink().matches("^(http|https)://.*$")) {
			super.state(context, false, "pictureLink", "acme.validation.Service.pictureLink.message");
			result = false;
		}

		// Validar que "avgDwellTime" sea un valor positivo
		if (service.getAvgDwellTime() == null || service.getAvgDwellTime() < 0) {
			super.state(context, false, "avgDwellTime", "acme.validation.Service.avgDwellTime.message");
			result = false;
		}

		// Validar que "promoCode" siga el patrón "^[A-Z]{4}-[0-9]{2}$" si está presente
		if (service.getPromoCode() != null && !service.getPromoCode().matches("^[A-Z]{4}-[0-9]{2}$")) {
			super.state(context, false, "promoCode", "acme.validation.Service.promoCode.message");
			result = false;
		}

		// Validar que "discount" sea un valor válido
		if (service.getDiscount() != null && service.getDiscount().getAmount() < 0) {
			super.state(context, false, "discount", "acme.validation.Service.discount.message");
			result = false;
		}

		return result;
	}
}
