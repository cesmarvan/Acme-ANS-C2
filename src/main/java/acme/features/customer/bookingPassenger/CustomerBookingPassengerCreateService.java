
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerCreateService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository	repository;

	@Autowired
	private CustomerBookingRepository			bookingRepository;

	@Autowired
	private CustomerPassengerRepository			passengerRepository;


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		Collection<Booking> bookings = this.bookingRepository.findBookingByCustomer(customerId);
		Collection<Passenger> passengers = this.repository.findAllPublishedPassengersByCustomerId(customerId);
		boolean isInBookings = true;
		boolean isInPassengers = true;

		if (super.getRequest().hasData("id")) {
			int bookingId = super.getRequest().getData("booking", int.class);
			if (bookingId != 0) {
				Booking booking = this.bookingRepository.findBookingById(bookingId);
				isInBookings = bookings.contains(booking);
			}

			int passengerId = super.getRequest().getData("passenger", int.class);
			if (passengerId != 0) {
				Passenger passenger = this.passengerRepository.findPassengerById(passengerId);
				isInPassengers = passengers.contains(passenger);
			}

		}
		super.getResponse().setAuthorised(isCustomer && isInPassengers && isInBookings);
	}

	@Override
	public void load() {
		BookingPassenger booking;

		booking = new BookingPassenger();

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final BookingPassenger bookingPassenger) {
		int BookingId;
		Booking booking;

		int PassengerId;
		Passenger passenger;

		BookingId = super.getRequest().getData("booking", int.class);
		booking = this.bookingRepository.findBookingById(BookingId);

		PassengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.passengerRepository.findPassengerById(PassengerId);

		super.bindObject(bookingPassenger);
		bookingPassenger.setBooking(booking);
		bookingPassenger.setPassenger(passenger);

	}

	@Override
	public void validate(final BookingPassenger bookingPassenger) {
		super.state(bookingPassenger.getBooking() != null, "booking", "acme.validation.confirmation.message.booking-record.create.booking");
		super.state(bookingPassenger.getPassenger() != null, "passenger", "acme.validation.confirmation.message.booking-record.create.passenger");

		if (bookingPassenger.getBooking() != null && bookingPassenger.getPassenger() != null) {
			BookingPassenger existing = this.repository.findBookingRecordBybookingIdpassengerId(bookingPassenger.getBooking().getId(), bookingPassenger.getPassenger().getId());
			super.state(existing == null, "*", "acme.validation.confirmation.message.booking-record.create");
		}
	}

	@Override
	public void perform(final BookingPassenger bookingPassenger) {
		this.repository.save(bookingPassenger);
	}

	@Override
	public void unbind(final BookingPassenger bookingPassenger) {
		Dataset dataset;
		SelectChoices passengerChoices;
		SelectChoices bookingChoices;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		Collection<Booking> bookings = this.bookingRepository.findBookingByCustomer(customerId);
		Collection<Passenger> passengers = this.passengerRepository.findPassengerByCustomer(customerId);
		bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingPassenger.getBooking());
		passengerChoices = SelectChoices.from(passengers, "fullName", bookingPassenger.getPassenger());

		dataset = super.unbindObject(bookingPassenger);
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookings", bookingChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
