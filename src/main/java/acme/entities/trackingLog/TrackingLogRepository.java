
package acme.entities.trackingLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId ORDER BY tl.resolutionPercentage desc")
	List<TrackingLog> findTrackingLogsByResolutionPercentage(int claimId);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);
}
