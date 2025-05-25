
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	public void initialize(final ValidFlightCrewMember constraintAnnotation) {
		// Initial setup if needed.
	}

	@Override
	public boolean isValid(final FlightCrewMember crewMember, final ConstraintValidatorContext context) {
		assert crewMember != null;
		boolean result = true;

		if (crewMember.getEmployeeCode() == null || !crewMember.getEmployeeCode().matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.employeeCode.message").addPropertyNode("employeeCode").addConstraintViolation();
			result = false;
		}

		else if (crewMember.getLanguageSkills() == null || crewMember.getLanguageSkills().isEmpty() || crewMember.getLanguageSkills().isBlank() || crewMember.getLanguageSkills().length() > 255) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.languageSkills.message").addPropertyNode("languageSkills").addConstraintViolation();
			result = false;
		}

		else if (crewMember.getPhoneNumber() == null || !crewMember.getPhoneNumber().matches("^\\+?\\d{6,15}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.phoneNumber.message").addPropertyNode("phoneNumber").addConstraintViolation();
			result = false;
		}

		else if (crewMember.getSalary() == null || crewMember.getSalary().getAmount() < 0 || crewMember.getSalary().getAmount() > 10000) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.salary.message").addPropertyNode("salary").addConstraintViolation();
			result = false;
		}

		else if (crewMember.getStatus() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.status.message").addPropertyNode("status").addConstraintViolation();
			result = false;
		} else if (crewMember.getYearsOfExperience() == null || crewMember.getYearsOfExperience() < 0 || crewMember.getYearsOfExperience() > 40) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.yearsOfExperience.message").addPropertyNode("yearsOfExperience").addConstraintViolation();
			result = false;
		} else if (crewMember.getAirline() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flightCrewMember.airline.message").addPropertyNode("airline").addConstraintViolation();
			result = false;
		}

		return result;

	}

}
