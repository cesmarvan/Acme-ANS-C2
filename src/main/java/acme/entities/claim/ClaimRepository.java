
package acme.entities.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.resolutionPercentage desc, t.updateMoment desc")
	Collection<TrackingLog> findAllByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage = 100.0")
	Collection<TrackingLog> findCompletedTrackingLogsByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage = :resolutionPercentage AND t.draftMode = false")
	Collection<TrackingLog> findTrackingLogsOfClaimByResolutionPercentage(int claimId, double resolutionPercentage);

}
