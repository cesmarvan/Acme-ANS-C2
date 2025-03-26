
package acme.features.authenticated.crew_member;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT * FROM FlightAssignment fa JOIN fa.leg l WHERE l.status = 'LANDED'")
	Collection<FlightAssignment> findFlightAssignmentCompletedLeg();

	@Query("SELECT * FROM FlightAssignment fa JOIN fa.leg l WHERE l.status = 'ON_TIME' OR l.status='DELAYED'")
	Collection<FlightAssignment> findPlannedFlightAssignment(Date arrival);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT * FROM FlightAssignment fa WHERE fa.flight_crew_member_id = :id")
	Collection<FlightAssignment> findFlightAssignmentByCrewMemberId(int id);
}
