
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhoneNumberValidator.class)
@ReportAsSingleViolation

public @interface ValidPhoneNumber {

	// Propiedades estándar de validación
	String message() default "{acme.validation.phone-number.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
