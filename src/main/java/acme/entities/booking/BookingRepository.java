
package acme.entities.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

public interface BookingRepository extends AbstractRepository {

	@Query("select bp.passenger from BookingPassenger bp where bp.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBooking(@Param("bookingId") Integer bookingId);

}
