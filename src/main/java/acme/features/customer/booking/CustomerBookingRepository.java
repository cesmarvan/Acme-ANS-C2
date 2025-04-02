
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(@Param("customerId") Integer customerId);

	@Query("select b from Booking b where b.flight.id = :flightId")
	Collection<Booking> findBookingsByFlightId(@Param("flightId") Integer flightId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(@Param("id") int id);

	@Query("select b from Booking b")
	Collection<Booking> findAllBookings();

	@Query("select c from Customer c  where c.id = :id")
	Collection<Customer> finCustomerById(int id);

	@Query("select b from Booking b where b.customer.userAccount.id = :customerId")
	Collection<Booking> findBookingByCustomer(@Param("customerId") int customerId);

	@Query("select bk.passenger from BookingPassenger bk where bk.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBooking(int bookingId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(@Param("customerId") Integer customerId);

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("select c from Customer c where c.userAccount.id = :accountId")
	Customer findCustomerByUserAccountId(int accountId);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false")
	Collection<Flight> findPublishedFlights();

}
