
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

// Definimos la anotación de validación para la entidad Aircraft

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AircraftValidator.class)
public @interface ValidAircraft {

	// Mensaje de error por defecto
	String message() default "acme.validation.Aircraft.invalid.message";

	// Grupos de validación
	Class<?>[] groups() default {};

	// Carga útil para transportar metadatos adicionales
	Class<? extends Payload>[] payload() default {};
}
