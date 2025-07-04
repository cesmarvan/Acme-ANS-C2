
package acme.features.flightCrewMember.ActivityLog;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

public interface ActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.flightCrewMember.id = :id")
	List<ActivityLog> findActivityLogsByFlightAssignmentId(int id);

	@Query("SELECT al FROM ActivityLog al WHERE al.id= :id")
	ActivityLog findActivityLogById(int id);

	@Query("SELECT cm FROM FlightCrewMember cm WHERE cm.id= :id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id= :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.draftMode = false")
	List<FlightAssignment> findAllPublishedFlightAssignments();

	@Query("SELECT fa FROM  FlightAssignment fa WHERE fa.flightCrewMember.id= :id AND fa.draftMode = false")
	List<FlightAssignment> findFlightAssignmentByCrewMemberId(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.leg.scheduledArrival < :currentDate")
	List<FlightAssignment> findAssignmentsByMemberIdCompletedLegs(Date currentDate, int id);

}
