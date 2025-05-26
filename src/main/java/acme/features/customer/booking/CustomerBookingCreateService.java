
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private FlightRepository			flightRepository;


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		boolean travelClass = true;

		Date today = MomentHelper.getCurrentMoment();
		boolean isFlightInList = true;
		Flight flight;

		if (super.getRequest().hasData("id")) {
			int flightId = super.getRequest().getData("flight", int.class);
			Collection<Flight> flights = this.repository.findAllPublishedFlightsWithFutureDeparture(today);

			if (flightId != 0) {
				flight = this.flightRepository.findFlightById(flightId);
				isFlightInList = flights.contains(flight);

			}

		}

		if (super.getRequest().hasData("travelClass", String.class)) {
			String travelClassData = super.getRequest().getData("travelClass", String.class);
			if (!"0".equals(travelClassData))
				try {
					TravelClass.valueOf(travelClassData);
				} catch (IllegalArgumentException e) {
					travelClass = false;
				}
		}

		super.getResponse().setAuthorised(isCustomer && isFlightInList && travelClass);
	}

	@Override
	public void load() {
		Booking booking;
		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int customerId = principal.getId();
		Customer customer = this.repository.findCustomerById(customerId);
		Date today = MomentHelper.getCurrentMoment();

		booking = new Booking();
		booking.setCustomer(customer);
		booking.setPurchaseMoment(today);
		booking.setIsPublished(false);

		super.getBuffer().addData(booking);
	}

	@Override
	public void validate(final Booking booking) {
		Booking b = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
		if (b != null)
			super.state(false, "locatorCode", "acme.validation.confirmation.message.booking.locator-code");
	}

	@Override
	public void perform(final Booking object) {
		this.repository.save(object);
	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "lastCreditCardNibble", "travelClass", "flight");

	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Collection<Flight> flights = this.flightRepository.findAllFlights();
		flightChoices = SelectChoices.from(flights, "description", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastCreditCardNibble", "price", "isPublished");
		dataset.put("travelClass", choices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}
