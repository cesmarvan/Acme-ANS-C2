
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

	private static final String PHONE_PATTERN = "^\\+?\\d{10,15}$";


	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value == null || value.trim().isEmpty())
			return true; // Vacío/Null = no está presente

		return value.matches(ValidPhoneNumberValidator.PHONE_PATTERN); // Solo valida si hay contenido
	}
}
