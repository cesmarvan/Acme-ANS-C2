
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentCompletedClaimsListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		boolean isAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAgent);
	}

	@Override
	public void load() {
		List<Claim> claims = new ArrayList<>();
		int AssistanceAgentId;

		AssistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<Claim> acceptedClaims = (List<Claim>) this.claimRepository.findAllAcceptedClaimsByAgentId(AssistanceAgentId);
		Collection<Claim> rejectedClaims = this.claimRepository.findAllRejectedClaimsByAgentId(AssistanceAgentId);

		claims.addAll(acceptedClaims);
		claims.addAll(rejectedClaims);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg");
		dataset.put("leg", claim.getLeg().getId());

		super.getResponse().addData(dataset);
	}

}
