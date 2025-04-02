
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerBookingRepository	bookingRepository;

	@Autowired
	private CustomerPassengerRepository	repository;


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Collection<Passenger> passengers;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		passengers = this.repository.findPassengerByCustomer(customerId);
		status = passengers.stream().allMatch(b -> b.getCustomer().getUserAccount().getId() == customerId) && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		int bookingId;

		bookingId = super.getRequest().getData("masterId", int.class);
		passengers = this.bookingRepository.findPassengersByBooking(bookingId);

		super.getBuffer().addData(passengers);
		//		super.getResponse().addGlobal("masterId", bookingId);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		int bookingId = super.getRequest().getData("masterId", int.class);

		dataset = super.unbindObject(passenger, "fullName", "email");
		dataset.put("masterId", bookingId);

		super.getResponse().addData(dataset);
	}
}
