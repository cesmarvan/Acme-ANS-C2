
package acme.features.customer.bookingPassenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingPassenger;

public interface CustomerBookingPassengerRepository extends AbstractRepository {

	@Query("SELECT bp FROM BookingPassenger bp WHERE bp.booking.id = :bookingId AND bp.passenger.id = :passengerId")
	BookingPassenger findBookingRecordById(@Param("bookingId") Integer bookingId, @Param("passengerId") Integer passengerId);
}
