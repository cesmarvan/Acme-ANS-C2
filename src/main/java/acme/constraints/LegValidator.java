
package acme.constraints;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

				boolean rightDatesOrder;

				rightDatesOrder = value.getScheduledArrival() == null || value.getScheduledDeparture() == null || value.getScheduledArrival().after(value.getScheduledDeparture());
				super.state(context, rightDatesOrder, "scheduledArrival", "acme.validation.leg.wrong-order-dates.message");
			}
			{
				boolean overlappingAircraft = true;

				if (value.getAircraft() != null) {

					List<Leg> legsSameAircraft = this.repository.findAllLegsByAircraftRegistrationNumber(value.getAircraft().getRegistrationNumber());

					legsSameAircraft = legsSameAircraft.stream().filter(legs -> !Objects.equals(legs.getFlightNumber(), value.getFlightNumber())).collect(Collectors.toList());

					overlappingAircraft = !legsSameAircraft.stream()
						.anyMatch(x -> x.getScheduledDeparture().compareTo(value.getScheduledDeparture()) <= 0 && x.getScheduledArrival().compareTo(value.getScheduledDeparture()) >= 0
							|| x.getScheduledDeparture().compareTo(value.getScheduledDeparture()) >= 0 && x.getScheduledArrival().compareTo(value.getScheduledArrival()) <= 0
							|| x.getScheduledDeparture().compareTo(value.getScheduledArrival()) <= 0 && x.getScheduledArrival().compareTo(value.getScheduledArrival()) >= 0);

				}
				super.state(context, overlappingAircraft, "aircraft", "acme.validation.leg.overlapping-aircraft.message");
			}
			{
				boolean sameAirport;

				sameAirport = value.getDepartureAirport() == null && value.getArrivalAirport() == null ? true : value.getDepartureAirport() != value.getArrivalAirport();

				super.state(context, sameAirport, "arrivalAirport", "acme.validation.leg.same-airport.message");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}

}
