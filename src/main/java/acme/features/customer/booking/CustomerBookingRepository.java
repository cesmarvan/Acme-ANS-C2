
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

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

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(Integer id);

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

	@Query("select f from Flight f where f.draftMode = false and exists (select l from Leg l where l.flight.id = f.id and l.scheduledDeparture > :today)")
	Collection<Flight> findAllPublishedFlightsWithFutureDeparture(Date today);

	@Query("select bk.passenger.fullName from BookingPassenger bk where bk.booking.id = :bookingId")
	Collection<String> findPassengersNameByBooking(@Param("bookingId") Integer bookingId);

	@Query("SELECT count(f)>0 FROM Flight f WHERE f.id = :id AND f.draftMode = false AND NOT EXISTS(SELECT l FROM Leg l WHERE l.flight = :id AND l.scheduledDeparture <= :currentMoment)")
	Boolean checkFlightIsAvailableById(Integer id, Date currentMoment);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false AND NOT EXISTS(SELECT l FROM Leg l WHERE l.flight = f AND l.scheduledDeparture <= :currentMoment)")
	Collection<Flight> findAvailableFlights(Date currentMoment);

	Optional<Booking> findByIdAndCustomerId(Integer id, Integer customerId);

}
