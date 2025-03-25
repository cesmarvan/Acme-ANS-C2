
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.flight.id = :flightId")
	Collection<Booking> findBookingsByFlightId(int flightId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b from Booking b")
	Collection<Booking> findAllBookings();

}
