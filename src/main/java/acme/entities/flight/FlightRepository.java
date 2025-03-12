
package acme.entities.flight;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	Optional<Leg> getFisrtLegOfFlight(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	Optional<Leg> getLastLegOfFlight(int flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer getNumberOfLegsOfFlight(int flightId);
}
