
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.customer.CustomerRepository;
import acme.realms.Customer;

public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "acme.validation.Customer.NotNull.message");
		else if (customer.getIdentifier() == null || !customer.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"))
			super.state(context, false, "Identifier", "acme.validation.Customer.identifier.message");
		else if (customer.getPhoneNumber() == null || !customer.getPhoneNumber().matches("^\\+?\\d{6,15}$"))
			super.state(context, false, "PhoneNumber", "acme.validation.Customer.phoneNumber.message");
		else if (customer.getAddress() == null || customer.getAddress().length() > 255 || customer.getAddress().length() < 0)
			super.state(context, false, "Address", "acme.validation.Customer.address.message");
		else if (customer.getCity() == null || customer.getCity().length() > 255 || customer.getCity().length() < 0)
			super.state(context, false, "City", "acme.validation.Customer.city.message");
		else if (customer.getCountry() == null || customer.getCountry().length() > 255 || customer.getCountry().length() < 0)
			super.state(context, false, "Country", "acme.validation.Customer.country.message");
		else if (customer.getEarnedPoints() != null && (customer.getEarnedPoints() < 0 || customer.getEarnedPoints() > 500000))
			super.state(context, false, "EarnedPoints", "acme.validation.Customer.earnedPoints.message");
		else {
			// Check for duplicate customer
			boolean uniqueCustomer;
			Customer existingCustomer = this.repository.findCustomerByIdentifier(customer.getIdentifier());
			uniqueCustomer = existingCustomer == null || existingCustomer.equals(customer);
			super.state(context, uniqueCustomer, "ticker", "acme.validation.customer.duplicated-identifier.message");

			// Check if the first two characters of the identifier match the customer's name and surname
			boolean firstCharacter = customer.getIdentifier().charAt(0) == customer.getIdentity().getName().charAt(0);
			boolean secondCharacter = customer.getIdentifier().charAt(1) == customer.getIdentity().getSurname().charAt(0);

			super.state(context, firstCharacter && secondCharacter, "identifier", "acme.validators.customer.correct-pattern");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
