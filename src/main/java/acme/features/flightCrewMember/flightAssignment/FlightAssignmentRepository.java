
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.CrewDuties;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa JOIN fa.leg l WHERE l.status = 'LANDED' AND fa.flightCrewMember.id = :id ")
	List<FlightAssignment> findFlightAssignmentCompletedLeg(int id);

	@Query("SELECT fa FROM FlightAssignment fa JOIN fa.leg l WHERE l.status = 'ON_TIME' OR l.status='DELAYED' AND  fa.flightCrewMember.id= :id")
	List<FlightAssignment> findPlannedFlightAssignment(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id= :id")
	List<FlightAssignment> findFlightAssignmentByCrewMemberId(int id);

	@Query("SELECT cm FROM FlightCrewMember cm WHERE cm.id = :id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("SELECT l FROM Leg l WHERE l.id= :id")
	Leg findLegById(int id);

	@Query("SELECT cm FROM FlightCrewMember cm")
	List<FlightCrewMember> findAllCrewMembers();

	@Query("SELECT l FROM Leg l")
	List<Leg> findAllLegs();

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE fa.flightCrewMember.id= :id")
	List<Leg> findLegsByCrewMemberId(int id);

	@Query("SELECT fa.flightCrewMember FROM  FlightAssignment fa WHERE fa.leg.id= :id")
	List<FlightCrewMember> findCrewMembersByLegId(int id);

	@Query("SELECT fa.duty FROM FlightAssignment fa WHERE fa.leg.id= :id")
	List<CrewDuties> findPilotsInLegByLegId(int id);
	//
	//	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.id= :id AND fa.duty = 'COPILOT'")
	//	List<FlightAssignment> findCopilotsInLegByLegId(int id);
}
