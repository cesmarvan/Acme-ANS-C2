
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Booking booking;
		Customer customer;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) || booking != null;

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
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditCardNibble");
		dataset.put("locatorCode", booking.getLocatorCode());
		dataset.put("purchaseMoment", booking.getPurchaseMoment());
		dataset.put("travelClass", booking.getTravelClass());
		dataset.put("price", booking.getPrice());
		dataset.put("lastCreditCardNibble", booking.getLastCreditCardNibble());
		super.getResponse().addData(dataset);
	}
}
