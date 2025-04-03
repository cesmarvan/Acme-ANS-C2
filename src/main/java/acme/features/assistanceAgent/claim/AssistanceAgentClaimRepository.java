
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("Select c FROM Claim c WHERE c.assistanceAgent.id = :AssistanceAgentId")
	Collection<Claim> findAllClaimsByAgentId(int AssistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.indicator = acme.entities.claim.IndicatorClaim.ACCEPTED AND c.assistanceAgent.id = :AssistanceAgentId")
	Collection<Claim> findAllAcceptedClaimsByAgentId(int AssistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.indicator = acme.entities.claim.IndicatorClaim.REJECTED AND c.assistanceAgent.id = :AssistanceAgentId")
	Collection<Claim> findAllRejectedClaimsByAgentId(int AssistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.indicator = acme.entities.claim.IndicatorClaim.PENDING AND c.assistanceAgent.id = :AssistanceAgentId")
	Collection<Claim> findUndergoingClaimsByAgentId(int AssistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT c.assistanceAgent FROM Claim c WHERE c.assistanceAgent.id = :AssistanceAgentId")
	AssistanceAgent findAssistanceAgentById(int AssistanceAgentId);

	@Query("SELECT c.leg FROM Claim c Where c.id = :id")
	Leg findLegByClaim(int id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();
}
