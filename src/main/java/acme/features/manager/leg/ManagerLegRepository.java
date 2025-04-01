
package acme.features.manager.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.Manager;

public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	Leg findFlightByFlightNumber(String flightNumber);

	@Query("select l from Leg l where l.flight.id = :id")
	List<Leg> findLegByFlightId(int id);

	@Query("select l from Leg l where l.flight.manager.id = :id")
	List<Leg> findAllLegsOfManagerById(int id);

	@Query("select l from Leg l where l.flight.id = :flightId and l.flight.manager.id = :managerId")
	List<Leg> findAllLegsByFlightAndManagerId(int flightId, int managerId);

	@Query("select l from Leg l where l.aircraft.registrationNumber = :registrationNumber")
	List<Leg> findAllLegsByAircraftRegistrationNumber(String registrationNumber);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	List<Leg> getFisrtLegOfFlight(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	List<Leg> getLastLegOfFlight(int flightId);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select a from Aircraft a")
	List<Aircraft> findAllAircrafts();

	@Query("select a from Airport a")
	List<Airport> findAllAirports();

	@Query("select m from Manager m where m.id = :id")
	Manager findManagerById(int id);
}
