
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.CrewDuties;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id= :id AND fa.leg.scheduledArrival < :now AND fa.leg.draftMode = false")
	List<FlightAssignment> findFlightAssignmentCompletedLeg(int id, Date now);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE fa.flightCrewMember.id= :id AND fa.leg.scheduledArrival < :now AND fa.leg.draftMode = false")
	List<Leg> findCompletedLegsOfCrewmember(int id, Date now);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id= :id AND fa.leg.scheduledArrival > :now AND fa.leg.draftMode = false")
	List<FlightAssignment> findPlannedFlightAssignment(int id, Date now);

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

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false")
	List<Leg> findAllPublishedLegs();

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false AND l.scheduledDeparture>:now")
	List<Leg> findAllUpcomingPublishedLegs(Date now);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE fa.flightCrewMember.id= :id")
	List<Leg> findLegsByCrewMemberId(int id);

	@Query("SELECT fa.flightCrewMember FROM  FlightAssignment fa WHERE fa.leg.id= :id")
	List<FlightCrewMember> findCrewMembersByLegId(int id);

	@Query("SELECT fa.duty FROM FlightAssignment fa WHERE fa.leg.id= :id")
	List<CrewDuties> findPilotsInLegByLegId(int id);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE (fa.leg.scheduledDeparture < :legArrival AND fa.leg.scheduledArrival > :legDeparture) AND fa.leg.id <> :legId AND fa.flightCrewMember.id = :id and fa.draftMode = false")
	List<Leg> findSimultaneousLegsByMember(Date legDeparture, Date legArrival, int legId, int id);
}
