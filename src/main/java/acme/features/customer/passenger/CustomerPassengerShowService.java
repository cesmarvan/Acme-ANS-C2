
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository	repository;

	@Autowired
	private CustomerBookingRepository	cBookingRepository;


	@Override
	public void authorise() {

		boolean status;
		int customerId;
		Collection<Booking> bookings;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		bookings = this.cBookingRepository.findBookingByCustomer(customerId);
		status = bookings.stream().allMatch(b -> b.getCustomer().getUserAccount().getId() == customerId);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Passenger passenger;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "isPublished", "specialNeeds");

		super.getResponse().addData(dataset);
	}
}
