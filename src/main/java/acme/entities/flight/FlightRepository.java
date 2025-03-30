
package acme.entities.flight;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	Optional<Leg> getFisrtLegOfFlight(@Param("flightId") int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	Optional<Leg> getLastLegOfFlight(@Param("flightId") int flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer getNumberOfLegsOfFlight(@Param("flightId") int flightId);

	@Query("select f.cost from Flight f where f.id = :flightId")
	Money findCostByFlight(@Param("flightId") Integer flightId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(@Param("flightId") Integer flightId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

}
