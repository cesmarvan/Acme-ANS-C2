
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentUndergoingClaimsListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		boolean isAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAgent);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int AssistanceAgentId;

		AssistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.claimRepository.findUndergoingClaimsByAgentId(AssistanceAgentId);

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
