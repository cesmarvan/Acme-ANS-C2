
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else if (claim.getLeg() != null) {
			boolean correctLeg;
			Leg leg = claim.getLeg();
			correctLeg = MomentHelper.compare(MomentHelper.getCurrentMoment(), leg.getScheduledArrival()) > 0 && leg.getDraftMode().equals(false) ? true : false;

			super.state(context, correctLeg, "leg", "acme.validation.claim.leg.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
