
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int customerId;
		Collection<Booking> bookings;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		bookings = this.repository.findBookingByCustomer(customerId);
		status = bookings.stream().allMatch(b -> b.getCustomer().getUserAccount().getId() == customerId);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		bookings = this.repository.findBookingByCustomer(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass");

		super.getResponse().addData(dataset);
	}

}
