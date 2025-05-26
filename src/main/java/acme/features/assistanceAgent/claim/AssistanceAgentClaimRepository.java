
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("Select c FROM Claim c WHERE c.assistanceAgent.id = :AssistanceAgentId")
	Collection<Claim> findAllClaimsByAgentId(int AssistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT c.assistanceAgent FROM Claim c WHERE c.assistanceAgent.id = :AssistanceAgentId")
	AssistanceAgent findAssistanceAgentById(int AssistanceAgentId);

	@Query("SELECT c.leg FROM Claim c Where c.id = :id")
	Leg findLegByClaim(int id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT l FROM Leg l WHERE l.id = :legId")
	Leg findLegById(int legId);

	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival < :currentMoment AND l.draftMode = false")
	Collection<Leg> findAvailableLegs(@Param("currentMoment") Date currentMoment);

	@Query("""
			SELECT COUNT(c) > 0
			FROM Claim c
			WHERE c.id = :claimId
			AND c.assistanceAgent.id = :agentId
			AND c.draftMode = true
		""")
	boolean isDraftClaimOwnedByAgent(@Param("claimId") int claimId, @Param("agentId") int agentId);
}
