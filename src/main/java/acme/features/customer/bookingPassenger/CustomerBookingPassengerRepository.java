
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;

public interface CustomerBookingPassengerRepository extends AbstractRepository {

	@Query("SELECT bp FROM BookingPassenger bp WHERE bp.booking.id = :bookingId AND bp.passenger.id = :passengerId")
	BookingPassenger findBookingPassengerById(@Param("bookingId") Integer bookingId, @Param("passengerId") Integer passengerId);

	@Query("select p from Passenger p where p.customer.userAccount.id = :customerId and p.isPublished = false")
	Collection<Passenger> findAllPublishedPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT br FROM BookingPassenger br WHERE br.booking.id = :bookingId AND br.passenger.id = :passengerId")
	BookingPassenger findBookingPassengerBybookingIdpassengerId(@Param("bookingId") Integer bookingId, @Param("passengerId") Integer passengerId);

	@Query("SELECT br FROM BookingPassenger br WHERE br.booking.id = :bookingId")
	Collection<BookingPassenger> findBookingPassengerByBookingId(@Param("bookingId") Integer bookingId);

}
