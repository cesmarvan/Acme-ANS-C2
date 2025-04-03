
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.Customer;

public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else if (customer.getIdentifier() == null || !customer.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"))
			super.state(context, false, "identifier", "acme.validation.Customer.identifier.message");
		else {
			String identifierCustomer = "";
			int count = 0;
			String[] nameList = customer.getUserAccount().getIdentity().getName().split(" ");
			count += nameList.length;
			for (Integer i = 0; i < nameList.length; i++)
				identifierCustomer += String.valueOf(nameList[i].charAt(0));

			String[] surnameList = customer.getUserAccount().getIdentity().getSurname().split(" ");
			for (Integer i = 0; i < surnameList.length && count < 3; i++) {
				identifierCustomer += String.valueOf(surnameList[i].charAt(0));
				count++;
			}

			if (customer.getIdentifier().substring(0, 2).equals(identifierCustomer) || customer.getIdentifier().substring(0, 3).equals(identifierCustomer))
				result = true;
			else
				super.state(context, false, "identifier", "acme.validation.Customer.identifier.name.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
