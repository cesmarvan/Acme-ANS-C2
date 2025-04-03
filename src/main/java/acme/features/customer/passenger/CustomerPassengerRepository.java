
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p WHERE p.id = :passengerId")
	Passenger findPassengerById(@Param("passengerId") Integer passengerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(@Param("customerId") Integer customerId);

	@Query("select p from Passenger p where p.customer.userAccount.id = :customerId")
	Collection<Passenger> findPassengerByCustomer(@Param("customerId") int customerId);

	@Query("select bp from BookingPassenger bp where bp.passenger.id = :passengerId")
	Collection<BookingPassenger> findBookingPassengerByPassenger(@Param("passengerId") int passengerId);

	@Query("select bk.booking from BookingPassenger bk where bk.passenger.id = :passengerId")
	Collection<Booking> findBookingByPassenger(int passengerId);

}
