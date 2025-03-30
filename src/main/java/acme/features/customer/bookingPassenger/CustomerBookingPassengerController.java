
package acme.features.customer.bookingPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.BookingPassenger;
import acme.realms.Customer;

@GuiController
public class CustomerBookingPassengerController extends AbstractGuiController<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerCreateService createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
	}
}
