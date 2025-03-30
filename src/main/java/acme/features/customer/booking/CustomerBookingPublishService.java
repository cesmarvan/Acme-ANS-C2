
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private FlightRepository			flightRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id;
		Booking booking;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		boolean status = booking.getCustomer().getUserAccount().getId() == customerId && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
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
		super.bindObject(booking, "locatorCode", "purchaseMoment", "price", "lastCreditCardNibble", "passengers", "travelClass");

	}

	@Override
	public void validate(final Booking booking) {
		if (booking.getLastCreditCardNibble() == null || booking.getLastCreditCardNibble().isBlank() || booking.getLastCreditCardNibble().isEmpty()) {
			String lastNibbleStored = this.repository.findBookingById(booking.getId()).getLastCreditCardNibble();
			if (lastNibbleStored == null || lastNibbleStored.isBlank() || lastNibbleStored.isEmpty())
				super.state(false, "lastNibble", "acme.validation.confirmation.message.lastCreditCardNibble");
		}
	}

	@Override
	public void perform(final Booking booking) {
		if (booking.getLastCreditCardNibble() == null || booking.getLastCreditCardNibble().isBlank() || booking.getLastCreditCardNibble().isEmpty())
			booking.setLastCreditCardNibble(this.repository.findBookingById(booking.getId()).getLastCreditCardNibble());
		booking.setIsPublished(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Collection<Flight> flights = this.flightRepository.findAllFlights();
		flightChoices = SelectChoices.from(flights, "description", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Passenger> passengersNumber = this.repository.findPassengersByBooking(booking.getId());
		Collection<String> passengers = passengersNumber.stream().map(x -> x.getFullName()).toList();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastCreditCardNibble", "isPublished");
		dataset.put("travelClass", choices);
		dataset.put("passengers", passengers);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}
