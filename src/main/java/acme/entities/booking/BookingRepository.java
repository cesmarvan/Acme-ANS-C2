
package acme.entities.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

public interface BookingRepository extends AbstractRepository {

	@Query("select bp.passenger from BookingPassenger bp where bp.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBooking(@Param("bookingId") Integer bookingId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode=: locatorCode")
	List<String> existsByLocatorCode(String locatorCode);

}
