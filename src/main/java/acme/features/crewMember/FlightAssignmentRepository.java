
package acme.features.crewMember;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa JOIN fa.leg l WHERE l.status = 'LANDED'")
	Collection<FlightAssignment> findFlightAssignmentCompletedLeg();

	@Query("SELECT fa FROM FlightAssignment fa JOIN fa.leg l WHERE l.status = 'ON_TIME' OR l.status='DELAYED'")
	Collection<FlightAssignment> findPlannedFlightAssignment(Date arrival);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentByCrewMemberId(int id);

	@Query("SELECT cm FROM FlightCrewMember cm WHERE cm.id = :id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("SELECT l FROM Leg l WHERE l.id= :id")
	Leg findLegById(int id);
}
