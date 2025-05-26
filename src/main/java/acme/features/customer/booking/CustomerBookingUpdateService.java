
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

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
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private FlightRepository			flightRepository;


	@Override
	public void authorise() {
		int id;
		Booking booking;
		boolean isFlightInList = true;
		boolean status = true;
		boolean travelClass = true;
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		status = booking.getCustomer().getUserAccount().getId() == customerId;

		try {

			if (!booking.getIsPublished() != false && status && isCustomer) {

				Date today = MomentHelper.getCurrentMoment();
				Integer flightId = super.getRequest().getData("flight", int.class);
				Collection<Flight> flights = this.repository.findAllPublishedFlightsWithFutureDeparture(today);
				Flight flight;

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
		} catch (Throwable E) {
			isFlightInList = false;
		}

		super.getResponse().setAuthorised(status && !booking.getIsPublished() && isFlightInList && isCustomer && travelClass);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "lastCreditCardNibble", "travelClass", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Date today = MomentHelper.getCurrentMoment();
		Collection<Flight> flights = this.repository.findAllPublishedFlightsWithFutureDeparture(today);
		flightChoices = SelectChoices.from(flights, "description", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<String> passengers = this.repository.findPassengersNameByBooking(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastCreditCardNibble", "price", "isPublished");
		dataset.put("travelClass", choices);
		dataset.put("passengers", passengers);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
