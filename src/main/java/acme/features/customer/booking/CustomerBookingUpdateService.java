
package acme.features.customer.booking;

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


	// ---------------------------------------------------------------------
	// Authorise
	// ---------------------------------------------------------------------
	@Override
	public void authorise() {
		boolean authorised = false;
		try {
			int bookingId = super.getRequest().getData("id", int.class);
			Booking booking = this.repository.findBookingById(bookingId);

			var principal = super.getRequest().getPrincipal();
			boolean isOwner = principal.hasRealmOfType(Customer.class) && booking.getCustomer().getUserAccount().getId() == principal.getActiveRealm().getUserAccount().getId();
			boolean isDraft = !booking.getIsPublished();

			boolean flightOk = true;
			if (super.getRequest().hasData("flight", int.class)) {
				int flightId = super.getRequest().getData("flight", int.class);
				if (flightId != 0) {
					Date today = MomentHelper.getCurrentMoment();
					Flight flight = this.flightRepository.findFlightById(flightId);
					flightOk = this.repository.findAllPublishedFlightsWithFutureDeparture(today).contains(flight);
				}
			}

			boolean classOk = true;
			if (super.getRequest().hasData("travelClass", String.class)) {
				String tc = super.getRequest().getData("travelClass", String.class);
				if (!"0".equals(tc))
					try {
						TravelClass.valueOf(tc);
					} catch (IllegalArgumentException e) {
						classOk = false;
					}
			}

			authorised = isOwner && isDraft && flightOk && classOk;
		} catch (Throwable __) {
			/* any failure ⇒ not authorised */ }
		super.getResponse().setAuthorised(authorised);
	}

	// ---------------------------------------------------------------------
	// Load / Bind / Validate / Perform
	// ---------------------------------------------------------------------
	@Override
	public void load() {
		super.getBuffer().addData(this.repository.findBookingById(super.getRequest().getData("id", int.class)));
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
		this.prepareDataset(booking);

	}
	@Override
	public void unbind(final Booking booking) {
		this.prepareDataset(booking);
	}

	private void prepareDataset(final Booking booking) {
		Date today = MomentHelper.getCurrentMoment();

		SelectChoices flightChoices = SelectChoices.from(this.repository.findAllPublishedFlightsWithFutureDeparture(today), "description", booking.getFlight());

		Dataset data = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastCreditCardNibble", "isPublished");           // campos que ya validan los tests

		data.put("travelClass", SelectChoices.from(TravelClass.class, booking.getTravelClass()));
		data.put("passengers", this.repository.findPassengersNameByBooking(booking.getId()));
		data.put("flight", flightChoices.getSelected().getKey());
		data.put("flights", flightChoices);

		super.getResponse().addData(data);
	}
}
