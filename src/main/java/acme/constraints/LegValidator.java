
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;
import acme.features.manager.leg.ManagerLegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg value, final ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		assert context != null;

		boolean result;

		if (value == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueLeg;
				Leg otherLeg;

				otherLeg = this.repository.findFlightByFlightNumber(value.getFlightNumber());
				uniqueLeg = otherLeg == null || otherLeg.equals(value);

				super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flight-number.message");
			}
			{
				boolean notNullAircraft;

				notNullAircraft = value.getAircraft() != null;

				super.state(context, notNullAircraft, "aircraft", "javax.validation.constraints.NotNull.message");
			}
			{

				boolean rightDatesOrder = true;

				if (value.getScheduledDeparture() == null)
					super.state(context, false, "scheduledDeparture", "acme.validation.leg.null-dates.message");
				else if (value.getScheduledArrival() == null)
					super.state(context, false, "scheduledArrival", "acme.validation.leg.null-dates.message");
				else {
					rightDatesOrder = value.getScheduledArrival().after(value.getScheduledDeparture());
					super.state(context, rightDatesOrder, "scheduledArrival", "acme.validation.leg.wrong-order-dates.message");
				}

			}
			{
				boolean sameAirport;

				sameAirport = value.getDepartureAirport() == null && value.getArrivalAirport() == null ? true : value.getDepartureAirport() != value.getArrivalAirport();

				super.state(context, sameAirport, "arrivalAirport", "acme.validation.leg.same-airport.message");
			}
			{
				boolean correctFlightNumber = false;

				if (value.getAircraft() == null)
					super.state(context, correctFlightNumber, "aircraft", "acme.validation.leg.null-aircraft.message");
				else {
					String airlineIATACode;
					airlineIATACode = value.getAircraft().getAirline().getIataCode();

					correctFlightNumber = value.getFlightNumber().contains(airlineIATACode) ? true : false;

					super.state(context, correctFlightNumber, "flightNumber", "acme.validation.leg.wrong-IATA-code.message");
				}
			}
		}
		result = !super.hasErrors(context);

		return result;
	}

}
