
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT tl.claim FROM TrackingLog tl WHERE tl.id = :id")
	Claim findClaimByTrackingLogId(int id);

	@Query("SELECT tl FROM TrackingLog tl ORDER BY tl.resolutionPercentage DESC")
	List<TrackingLog> findTrackingLogsOrderByResolutionPercentage();
}
