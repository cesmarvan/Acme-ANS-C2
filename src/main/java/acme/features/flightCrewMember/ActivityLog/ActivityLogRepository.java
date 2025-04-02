
package acme.features.flightCrewMember.ActivityLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

public interface ActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM  ActivityLog al JOIN FlightAssignment fa ON fa.flightCrewMember.id= :id")
	List<ActivityLog> findActivityLogsByFlightCrewMemberId(int id);

	@Query("SELECT al FROM ActivityLog al WHERE al.id= :id")
	ActivityLog findActivityLogById(int id);

	@Query("SELECT cm FROM FlightCrewMember cm WHERE cm.id= :id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id= :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa")
	List<FlightAssignment> findAllFlightAssignments();

	@Query("SELECT fa FROM  FlightAssignment fa WHERE fa.flightCrewMember.id= :id")
	FlightAssignment findFlightAssignmentByCrewMemberId(int id);

}
