
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = AirlineValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidAirline {

	// Mensaje de error por defecto
	String message() default "acme.validation.Airline.invalid.message";

	// Grupos de validación
	Class<?>[] groups() default {};

	// Carga útil para transportar metadatos adicionales
	Class<? extends Payload>[] payload() default {};
}
